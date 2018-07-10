package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.data.entitiesModel.SignupModel

/**
 * Created by Adhamkh on 2018-06-15.
 */
class SignupContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun trySignUp()
        fun signUp(signupModel: SignupModel)
    }

    interface View : BaseContract.View {
        fun checkValidation(): Boolean
        fun getUser(): SignupModel
        fun NavigateAfterSignUp(user: User)

        fun signupFail()
        fun signupduplicate(duplicateModel: DuplicateModel)
        fun signupSuccesfully(user: User)
        fun signup2Business(user: User)
    }
}