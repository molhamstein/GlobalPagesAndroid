package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entitiesModel.PostModel

/**
 * Created by Adhamkh on 2018-10-09.
 */
class PostContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun addPost(postModel: PostModel, token: String)

    }

    interface View : BaseContract.View {
        fun onAddPostSuccessfully()
        fun onAddPostFail()

    }

}