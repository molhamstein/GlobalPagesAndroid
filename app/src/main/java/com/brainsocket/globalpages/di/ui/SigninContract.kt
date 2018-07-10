package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.data.entitiesModel.LoginModel

/**
 * Created by Adhamkh on 2018-06-14.
 */
class SigninContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun authenticate(loginModel: LoginModel)
    }

    interface View : BaseContract.View {
        fun loginSuccesfully(loginResponse: LoginResponse)
        fun loginFail()
    }

}