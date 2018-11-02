package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.data.entitiesModel.LoginModel

class SigninContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun authenticate(loginModel: LoginModel)
    }

    interface View : BaseContract.View {
        fun loginSuccessfully(loginResponse: LoginResponse)
        fun loginFail()
    }

}