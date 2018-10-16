package com.brainsocket.globalpages.viewModel

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entitiesModel.ProfileModel
import com.brainsocket.globalpages.data.mapping.UserProfileMapper
import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.normalization.DateNormalizer
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.BindingUtils

class ProfileEditViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var context: Context

    @BindView(R.id.userNameEditText)
    lateinit var userNameEditText: EditText

    @BindView(R.id.birthDate)
    lateinit var birthDate: EditText

    @BindView(R.id.phoneNumber)
    lateinit var phoneNumber: EditText

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

    @BindView(R.id.imageProfileUrl)
    lateinit var imageProfileUrl: TextView

    @BindView(R.id.profileImage)
    lateinit var profileImage: ImageView

    @BindView(R.id.birthDateLayout)
    lateinit var birthDateLayout: TextInputLayout

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun getDefaultProfileModel(): ProfileModel =
            UserProfileMapper.userProfileTransform(UserRepository(context = context).getUser()!!)

    fun getProfileModel(): ProfileModel {
        val profileModel = getDefaultProfileModel()
        profileModel.username = userNameEditText.text.toString()
        profileModel.birthDate = birthDate.text.toString()
        profileModel.phoneNumber = phoneNumber.text.toString()

        if ((genderTabLayout.selectedTabPosition == 0))
            profileModel.gender = UserGender.male.gender
        else
            profileModel.gender = UserGender.female.gender

        profileModel.imageProfile = imageProfileUrl.text.toString()

        return profileModel
    }

    fun isValid(): Boolean {

        if (userNameEditText.text.toString().trim().isEmpty()) {
            userNameEditText.error = (context.resources.getString(R.string.enterusername))
            userNameEditText.requestFocus()
            return false
        }

        return true
    }

    fun bindView(profileModel: ProfileModel) {
        userNameEditText.setText(profileModel.username)
        birthDate.setText(profileModel.birthDate)
        phoneNumber.setText(profileModel.phoneNumber)

        if(profileModel.gender!=null){
            if(profileModel.gender!!.equals(UserGender.male.gender)){
                genderTabLayout.getTabAt(0)!!.select()
            }else{
                genderTabLayout.getTabAt(1)!!.select()
            }
        }else{
            genderTabLayout.getTabAt(0)!!.select()
        }
        imageProfileUrl.text = profileModel.imageProfile
        BindingUtils.loadProfileImage(profileImage,profileModel.imageProfile)
    }

    fun setImageUrl(url: String) {
        imageProfileUrl.text = url
        BindingUtils.loadProfileImage(profileImage,url)
    }

}