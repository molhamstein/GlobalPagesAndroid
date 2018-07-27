package com.brainsocket.globalpages.viewHolders

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.data.entitiesModel.SignUpModel
import com.brainsocket.globalpages.data.validations.ValidationHelper
import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.utilities.mainHelper

/**
 * Created by Adhamkh on 2018-06-15.
 */
class SignUpViewHolder : RecyclerView.ViewHolder {
    var context: Context

    constructor(view: View) : super(view) {
        context = view.context
        ButterKnife.bind(this, view)
    }

    @BindView(R.id.email)
    lateinit var email: EditText

    @BindView(R.id.userName)
    lateinit var userName: EditText

    @BindView(R.id.password)
    lateinit var password: EditText

    @BindView(R.id.birthdate)
    lateinit var birthdate: EditText

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

    @BindView(R.id.acceptTerms)
    lateinit var acceptTerms: CheckBox

    @BindView(R.id.businessOwnerCheckBox)
    lateinit var businessOwnerCheckBox: CheckBox

    @BindView(R.id.birthdateLayout)
    lateinit var birthdateLayout: TextInputLayout


    fun isValid(): Boolean {
        var signUpModel = getSignUpModel()
        if (ValidationHelper.isEmpty(signUpModel.email)) {
            email.error = context.resources.getString(R.string.enteremail)
            email.requestFocus()
            return false
        }
        if (!ValidationHelper.isEmail(signUpModel.email)) {
            email.error = context.resources.getString(R.string.entercorrectemail)
            email.requestFocus()
            return false
        }
        if (ValidationHelper.isNullorEmpty(signUpModel.username)) {
            userName.error = context.resources.getString(R.string.enterusername)
            userName.requestFocus()
            return false
        }
        if (ValidationHelper.isNullorEmpty(signUpModel.password)) {
            password.error = context.resources.getString(R.string.enterpassword)
            password.requestFocus()
            return false
        }

//        if (!ValidationHelper.isDate(signUpModel.birthdate!!)) {
        if ((signUpModel.birthdate == null) || (signUpModel.birthdate!!.isEmpty())) {
//            birthdate.error = context.resources.getString(R.string.selectBirthDate)
            birthdateLayout.error = context.resources.getString(R.string.selectBirthDate)
//            birthdate.requestFocus()
            return false
        }

        if (!signUpModel.termAndCondition) {
            mainHelper.hideKeyboard(itemView)
            Toast.makeText(context, R.string.accepttermsandconditions, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun getSignUpModel(): SignUpModel {
        var signUpModel = SignUpModel()
        signUpModel.email = email.text.toString()
        signUpModel.username = userName.text.toString()
        signUpModel.password = password.text.toString()
        signUpModel.birthdate = birthdate.text.toString()
        if ((genderTabLayout.selectedTabPosition == 0))
            signUpModel.gender = UserGender.male.gender
        else
            signUpModel.gender = UserGender.female.gender

        signUpModel.termAndCondition = acceptTerms.isChecked

        return signUpModel
    }

    fun checkDuplicate(duplicateModel: DuplicateModel) {
        if (duplicateModel.duplicateEmail) {
            email.error = (context.resources.getString(R.string.AlreadyExisted))
            email.requestFocus()
        }
        if (duplicateModel.duplicateUserName) {
            userName.error = (context.resources.getString(R.string.AlreadyExisted))
            userName.requestFocus()
        }
    }


}