package com.almersal.android.di.ui

import com.almersal.android.data.entitiesModel.ForgotPasswordModel

class ForgotPasswordContract {


    interface Presenter : BaseContract.Presenter<View> {
        fun requestPassword(forgotPasswordModel: ForgotPasswordModel)
    }

    interface View : BaseContract.View {
        fun sendSuccessfully()
        fun requestFail()
    }

}