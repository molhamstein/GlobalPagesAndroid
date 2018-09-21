package com.brainsocket.globalpages.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView

import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.di.component.DaggerProfileEditComponent
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.ProfileModule
import com.brainsocket.globalpages.di.ui.AttachmentContract
import com.brainsocket.globalpages.di.ui.AttachmentPresenter
import com.brainsocket.globalpages.di.ui.ProfileContract
import com.brainsocket.globalpages.di.ui.ProfilePresenter
import com.brainsocket.globalpages.dialogs.ProgressDialog
import com.brainsocket.globalpages.normalization.DateNormalizer
import com.brainsocket.globalpages.viewModel.ProfileEditViewHolder
import com.brainsocket.globalpages.views.CustomTabView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.google.android.gms.location.places.ui.PlacePicker
import java.io.File
import java.util.*

class ProfileEditActivity : BaseActivity(), ProfileContract.View, AttachmentContract.View {

    lateinit var profileEditViewHolder: ProfileEditViewHolder

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    @Inject
    lateinit var profilePresenter: ProfilePresenter

    private fun initDI() {
        val component = DaggerProfileEditComponent.builder()
                .profileModule(ProfileModule(this))
                .attachmentModule(AttachmentModule(this))
                .build()
        component.inject(this)
        attachmentPresenter.attachView(this)

        profilePresenter.attachView(this)
    }

    private fun initToolbar() {
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { onBackPressed() }
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
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            }
        })

        femaleTab.select()
        maleTab.select()

    }

    private fun initDatePicker() {
        val date: java.util.GregorianCalendar = java.util.GregorianCalendar(Locale.ENGLISH)
        val datePicker = DatePickerDialog(this, R.style.AppTheme_DialogSlideAnimwithback,
                DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int,
                                                     dayOfMonth: Int ->
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.MONTH, month)
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    profileEditViewHolder.birthDate.setText(DateNormalizer.getCanonicalDateFormat(date = date.time))
                    profileEditViewHolder.birthDateLayout.error = null
                }
                , date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
        datePicker.show()
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_edit_layout)
        ButterKnife.bind(this)
        profileEditViewHolder = ProfileEditViewHolder(findViewById(android.R.id.content))
        initDI()
        initTabLayout()
        initToolbar()

        profileEditViewHolder.bindView(profileEditViewHolder.getDefaultProfileModel())

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                BusinessGuideAddActivity.PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                }
                BusinessGuideAddActivity.PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
                    Log.v("", "")
                }
                BusinessGuideAddActivity.MAP_BUTTON_REQUEST_CODE -> {
                    Log.v("", "")
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this@ProfileEditActivity, BusinessGuideAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(this@ProfileEditActivity, resources.getString(R.string.Approve_Permissions_To_Pick_Images), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    @Optional
    @OnClick(R.id.birthDateLayout, R.id.birthDate)
    fun onBirthDateClick(view: View) {
        initDatePicker()
    }

    @OnClick(R.id.profileImage)
    fun onProfileImageClick(view: View) {
        Pix.start(this@ProfileEditActivity, BusinessGuideAddActivity.PICTURE_REQUEST, 1)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.updateProfileBtn)
    fun onUpdateProfileButtonClick(view: View) {
        if (profileEditViewHolder.isValid()) {
            profilePresenter.updateProfile(profileEditViewHolder.getProfileModel())
        }

        Log.v("View Clicked", view.id.toString())
    }


    /*Attachment presenter started*/
    override fun showAttachmentProgress(show: Boolean) {
        if (show) {
            val progressDialog: ProgressDialog = ProgressDialog.newInstance()
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        } else {
            val dialog = supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
            dialog?.dialog?.dismiss()
        }
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {

    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
    }

    override fun showAttachmentEmptyView(visible: Boolean) {

    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        profileEditViewHolder.setImageUrl(filePath)
    }
    /*Attachment presenter ended*/


    /*Profile presenter started*/
    override fun showUpdateProfileProgress(show: Boolean) {
        if (show)
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        else
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun showUpdateProfileLoadErrorMessage(visible: Boolean) {
        if (visible)
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        else
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun showUpdateProfileEmptyView(visible: Boolean) {
        if (visible)
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        else
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun onUpdateProfileSuccessfully() {
        Log.v("", "")
    }
    /*Profile presenter ended*/


}