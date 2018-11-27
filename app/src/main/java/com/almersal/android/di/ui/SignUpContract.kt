package com.almersal.android.di.ui

import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesModel.DuplicateModel
import com.almersal.android.data.entitiesModel.SignUpModel

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