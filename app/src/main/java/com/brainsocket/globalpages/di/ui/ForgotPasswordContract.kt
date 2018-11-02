package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entitiesModel.ForgotPasswordModel

class ForgotPasswordContract {


    interface Presenter : BaseContract.Presenter<View> {
        fun requestPassword(forgotPasswordModel: ForgotPasswordModel)
    }

    interface View : BaseContract.View {
        fun sendSuccessfully()
        fun requestFail()
    }

}