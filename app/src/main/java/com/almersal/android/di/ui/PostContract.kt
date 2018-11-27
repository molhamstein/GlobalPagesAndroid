package com.almersal.android.di.ui

import com.almersal.android.data.entities.Post
import com.almersal.android.data.entitiesModel.PostEditModel
import com.almersal.android.data.entitiesModel.PostModel

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