package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entitiesModel.PostEditModel
import com.brainsocket.globalpages.data.entitiesModel.PostModel

/**
 * Created by Adhamkh on 2018-10-09.
 */
class PostContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun addPost(postModel: PostModel, token: String)
        fun updatePost(postModel: PostEditModel, token: String)

        fun loadFeaturedPost()
    }

    interface View : BaseContract.View {
        fun onAddPostSuccessfully(post: Post) {}
        fun onAddPostFail() {}

        fun onUpdatePostSuccessfully(post: Post) {}
        fun onUpdatePostFail() {}

        fun showFeaturedPostProgress(show: Boolean) {}
        fun showFeaturedPostLoadErrorMessage(visible: Boolean) {}
        fun showFeaturedPostEmptyView(visible: Boolean) {}
        fun onFeaturedPostLoadedSuccessfully(postList: MutableList<Post>) {}

    }

}