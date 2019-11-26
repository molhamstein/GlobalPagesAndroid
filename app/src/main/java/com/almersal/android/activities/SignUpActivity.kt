package com.almersal.android.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.data.entitiesModel.SignUpModel
import com.almersal.android.di.component.DaggerSignUpComponent
import com.almersal.android.di.module.SignUpModule
import com.almersal.android.di.ui.SignUpContract
import com.almersal.android.di.ui.SignUpPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.views.CustomTabView
import com.almersal.android.viewModel.SignUpViewHolder
import java.util.*
import javax.inject.Inject
import android.content.DialogInterface
import android.support.v4.content.ContextCompat
import android.widget.*
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesModel.DuplicateModel
import com.almersal.android.data.entitiesModel.LoginModel
import com.almersal.android.data.entitiesResponses.LoginResponse
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.ui.NotificationContract
import com.almersal.android.di.ui.NotificationPresenter
import com.almersal.android.repositories.SettingData
import com.almersal.android.repositories.SettingRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.MainHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult

class SignUpActivity : BaseActivity(), SignUpContract.View, NotificationContract.View {

    @Inject
    lateinit var presenter: SignUpPresenter

    @Inject
    lateinit var notificationPresenter: NotificationPresenter

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

    @BindView(R.id.birthdate)
    lateinit var birthdate: EditText

    private lateinit var viewHolder: SignUpViewHolder

    private lateinit var progressDialog: ProgressDialog

    private fun initDI() {
        val component = DaggerSignUpComponent.builder()
                .signUpModule(SignUpModule(this))
                .notificationModule(NotificationModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

        notificationPresenter.attachView(this)
        notificationPresenter.subscribe()

    }

    private fun initTabLayout() {
        val maleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.male, R.mipmap.ic_male_24dp)).setText(R.string.male)
        val femaleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.female, R.mipmap.ic_female_24dp)).setText(R.string.female)

        genderTabLayout.addTab(maleTab)
        genderTabLayout.addTab(femaleTab)

        genderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.v("", "")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.grayLightTextColor))
                tab?.customView?.findViewById<ImageView>(R.id.gender_icon)?.alpha = 0.3f
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.grayDarkTextColor))
                tab?.customView?.findViewById<ImageView>(R.id.gender_icon)?.alpha = 1.0f
            }
        })

        femaleTab.select()
        maleTab.select()

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.signup_layout)
        ButterKnife.bind(this)

        initDI()

        initTabLayout()

        viewHolder = SignUpViewHolder(this.findViewById(R.id.rootView))
        progressDialog = ProgressDialog.newInstance()

    }

    @OnClick(R.id.birthdate)
    fun onBirthDateClick(view: View) {
        initDatePicker()
        Log.v("View Clicked", view.id.toString())
    }

    private fun initDatePicker() {
        val date: java.util.GregorianCalendar = java.util.GregorianCalendar(Locale.ENGLISH)
        val datePicker = DatePickerDialog(this, R.style.AppTheme_DialogSlideAnimwithback,
                DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int,
                                                     dayOfMonth: Int ->
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.MONTH, month)
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    birthdate.setText(DateNormalizer.getCanonicalDateFormat(date = date.time))
                    viewHolder.birthdateLayout.error = null
                }
                , date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    @OnClick(R.id.signUpBtn)
    fun onSignUpClick(view: View) {
        presenter.trySignUp()
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.loginBtn)
    fun onSignInClick(view: View) {
        IntentHelper.startSignInActivity(SignUpActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.termsOfUseLayout)
    fun onTermsOfUseLayoutClick(view: View) {
        IntentHelper.startWebSite(baseContext, SettingData.siteAddress)
    }


    /*Presenter Started*/
    override fun checkValidation(): Boolean {
        return viewHolder.isValid()
    }

    override fun getUser(): SignUpModel {
        return viewHolder.getSignUpModel()
    }

    override fun navigateAfterSignUp(user: User) {
        UserRepository(context = this).addUser(user)
        val builder = AlertDialog.Builder(SignUpActivity@ this)
        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    signUp2Business(user)

                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    signUpSuccessfully(user)
                }
            }
        }
        builder.setMessage(resources.getString(R.string.doYouWantAddFirstBusiness))
                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()
        // return viewHolder.businessOwnerCheckBox.isChecked
    }

    override fun signUpDuplicate(duplicateModel: DuplicateModel) {
        viewHolder.checkDuplicate(duplicateModel)
    }

    override fun signUpSuccessfully(user: User) {

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { p0 ->
            if (p0 != null)
                SettingRepositories(this@SignUpActivity).addToken(p0.token)
        }
        val fireBaseToken: String? = SettingRepositories(this).getToken()
        val token: String = UserRepository(this).getUser()!!.token
        if (fireBaseToken != null) {
            notificationPresenter.registerFireBaseToken(fireBaseToken = fireBaseToken, token = token)
        }

        presenter.authenticate(LoginModel(getUser().email,getUser().password))

        finish()
        Log.v("", "")
    }

    override fun signUp2Business(user: User) {
        IntentHelper.startBusinessAddActivity(this)
        finish()
        Log.v("", "")
    }

    override fun signUpFail() {
        Toast.makeText(baseContext, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressDialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.closeDialog(supportFragmentManager)
        }
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        }
    }

    override fun loginSuccessfully(loginResponse: LoginResponse) {
        loginResponse.user.token = loginResponse.id
        UserRepository(context = this).addUser(loginResponse.user)
//        FirebaseMessaging.getInstance().subscribeToTopic(Utl.client.getUser().getUid())

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { p0 ->
            if (p0.isSuccessful) {
                val token: String? = p0.result?.token
                if (token != null)
                    SettingRepositories(this@SignUpActivity).addToken(token)
            } else {
                Log.v("AAA", "")
            }
            //                this.sendRegistrationToServer(token)
        }

        val fireBaseToken: String? = SettingRepositories(this).getToken()
        val token: String = UserRepository(this).getUser()!!.token
        if (fireBaseToken != null) {
            notificationPresenter.registerFireBaseToken(fireBaseToken = fireBaseToken, token = token)
        }
        IntentHelper.startMainActivity(this)
        Log.v("", "")
    }

    override fun loginFail() {
        MainHelper.hideKeyboard(findViewById(android.R.id.content))
        Toast.makeText(baseContext, R.string.invalidEmailorPassword, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    /*Presenter ended*/

}