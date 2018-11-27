package com.almersal.android.di.ui

import com.almersal.android.data.entitiesResponses.LoginResponse
import com.almersal.android.data.entitiesModel.LoginModel

class SigninContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun authenticate(loginModel: LoginModel)
    }

    interface View : BaseContract.View {
        fun loginSuccessfully(loginResponse: LoginResponse)
        fun loginFail()
    }

}