package com.brainsocket.globalpages.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entitiesModel.SignupModel
import com.brainsocket.globalpages.di.component.DaggerSignupComponent
import com.brainsocket.globalpages.di.module.SignupModule
import com.brainsocket.globalpages.di.ui.SignupContract
import com.brainsocket.globalpages.di.ui.SignupPresenter
import com.brainsocket.globalpages.dialogs.ProgressDialog
import com.brainsocket.globalpages.normalization.DateNormalizer
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.views.CustomTabView
import com.brainsocket.globalpages.viewHolders.SignupViewHolder
import java.util.*
import javax.inject.Inject
import android.content.DialogInterface
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.repositories.userRepository


/**
 * Created by Adhamkh on 2018-06-08.
 */
class SignupActivity : BaseActivity(), SignupContract.View {

    @Inject
    lateinit var presenter: SignupPresenter

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

    @BindView(R.id.birthdate)
    lateinit var birthdate: EditText

    lateinit var viewHolder: SignupViewHolder

    lateinit var progressDialog: ProgressDialog

    fun initDI() {
        val component = DaggerSignupComponent.builder()
                .signupModule(SignupModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

    }

    fun initTabLayout() {
        var maleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.male, R.mipmap.ic_male_24dp)).setText(R.string.male)
        var femaleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.female, R.mipmap.ic_female_24dp)).setText(R.string.female)

        genderTabLayout.addTab(maleTab)
        genderTabLayout.addTab(femaleTab)

        genderTabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.v("", "")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(baseContext.resources.getColor(R.color.grayDarkTextColor))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(baseContext.resources.getColor(R.color.colorPrimary))
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

        viewHolder = SignupViewHolder(this.findViewById(R.id.rootView))
        progressDialog = ProgressDialog.newInstance()

    }

    @OnClick(R.id.birthdate)
    fun onBirthDateClick(view: View) {
        initDatePicker()
    }

    fun initDatePicker() {
        var date: java.util.GregorianCalendar = java.util.GregorianCalendar(Locale.ENGLISH)
        val datePicker = DatePickerDialog(this, R.style.AppTheme_DialogSlideAnimwithback,
                DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, month: Int,
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

    @OnClick(R.id.signupBtn)
    fun onSignUpClick(view: View) {
        presenter.trySignUp()
    }

    @OnClick(R.id.loginBtn)
    fun onSignInClick(view: View) {
        intentHelper.startSignInActivity(SignupActivity@ this)
    }

    /*Presenter Started*/
    override fun checkValidation(): Boolean {
        return viewHolder.isValid()
    }

    override fun getUser(): SignupModel {
        return viewHolder.getSignupModel()
    }

    override fun NavigateAfterSignUp(user: User) {
        userRepository(context = this).addUser(user)
        val builder = AlertDialog.Builder(SignupActivity@ this)
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    signup2Business(user)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    signupSuccesfully(user)
                }
            }
        }

        builder.setMessage(resources.getString(R.string.doYouWantAddFirstBusiness))
                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()
        // return viewHolder.businessOwnerCheckBox.isChecked
    }

    override fun signupduplicate(duplicateModel: DuplicateModel) {
        viewHolder.checkDuplicate(duplicateModel)
    }

    override fun signupSuccesfully(user: User) {
        intentHelper.startMainActivity(this)
        Log.v("", "")
    }

    override fun signup2Business(user: User) {
        intentHelper.startBusinessAddActivity(this)
        Log.v("", "")
    }

    override fun signupFail() {

        Log.v("", "")
    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        } else {
            progressDialog.dismiss()
        }
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        }
    }

    /*Presenter ended*/
}