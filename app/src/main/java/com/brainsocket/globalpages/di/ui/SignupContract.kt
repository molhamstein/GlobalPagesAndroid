package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.data.entitiesModel.SignUpModel

/**
 * Created by Adhamkh on 2018-06-15.
 */
class SignUpContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun trySignUp()
        fun signUp(signUpModel: SignUpModel)
    }

    interface View : BaseContract.View {
        fun checkValidation(): Boolean
        fun getUser(): SignUpModel
        fun navigateAfterSignUp(user: User)

        fun signUpFail()
        fun signUpDuplicate(duplicateModel: DuplicateModel)
        fun signUpSuccessfully(user: User)
        fun signUp2Business(user: User)
    }
}