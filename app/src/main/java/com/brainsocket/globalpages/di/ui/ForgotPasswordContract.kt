package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entitiesModel.ForgotPasswordModel

/**
 * Created by Adhamkh on 2018-06-18.
 */
class ForgotPasswordContract {


    interface Presenter : BaseContract.Presenter<View> {
        fun requestPassword(forgotPasswordModel: ForgotPasswordModel)
    }

    interface View : BaseContract.View {
        fun sendSuccesfully()
        fun requestFail()
    }

}