package com.almersal.android.di.ui

import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import java.io.File

class UserResumeContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun getUserResume(userId: String)

    }

    interface View : BaseContract.View {


        fun updateUserInfo(user: User?)
        fun getFailed()

    }
}