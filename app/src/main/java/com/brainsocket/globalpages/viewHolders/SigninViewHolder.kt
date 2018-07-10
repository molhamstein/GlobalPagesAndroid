package com.brainsocket.globalpages.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entitiesModel.LoginModel
import com.brainsocket.globalpages.data.validations.ValidationHelper

/**
 * Created by Adhamkh on 2018-06-15.
 */
class SigninViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.email)
    lateinit var email: EditText

    @BindView(R.id.password)
    lateinit var password: EditText

    var context: Context

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun isValid(): Boolean {
        var loginModel = getLoginModel()
        if (ValidationHelper.isEmpty(loginModel.email)) {
            email.error = context.resources.getString(R.string.enteremail)
            email.requestFocus()
            return false
        }
        if (!ValidationHelper.isEmail(loginModel.email)) {
            email.error = context.resources.getString(R.string.entercorrectemail)
            email.requestFocus()
            return false
        }
        if (ValidationHelper.isEmpty(loginModel.password)) {
            password.error = context.resources.getString(R.string.enterpassword)
            email.requestFocus()
            return false
        }
        return true
    }

    fun getLoginModel(): LoginModel {
        var loginModel = LoginModel()
        loginModel.email = email.text.toString()
        loginModel.password = password.text.toString()
        return loginModel
    }


}