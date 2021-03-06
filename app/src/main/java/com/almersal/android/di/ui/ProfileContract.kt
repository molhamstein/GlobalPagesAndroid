package com.almersal.android.di.ui

import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.Category
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesModel.ProfileModel

class ProfileContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadUserPosts(userId: String /*criteria: MutableMap<String, Pair<String, String>>*/)
        fun loadUserBusinesses(userId: String /*criteria: MutableMap<String, Pair<String, String>>*/)
        fun loadUserCategories(user: User)

        fun updateProfile(profileModel: ProfileModel, token: String)
    }

    interface View : BaseContract.View {

        fun showUserCategoriesProgress(show: Boolean) {}
        fun showUserCategoriesLoadErrorMessage(visible: Boolean) {}
        fun showUserCategoriesEmptyView(visible: Boolean) {}
        fun onUserCategoriesListSuccessfully(categories: MutableList<Category>) {}


        fun showUserPostsProgress(show: Boolean) {}
        fun showUserPostsLoadErrorMessage(visible: Boolean) {}
        fun showUserPostsEmptyView(visible: Boolean) {}
        fun onUserPostsListSuccessfully(postList: MutableList<Post>) {}


        fun showUserBusinessesProgress(show: Boolean) {}
        fun showUserBusinessesLoadErrorMessage(visible: Boolean) {}
        fun showUserBusinessesEmptyView(visible: Boolean) {}
        fun onUserBusinessesListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {}

        fun showUpdateProfileProgress(show: Boolean) {}
        fun showUpdateProfileLoadErrorMessage(visible: Boolean) {}
        fun showUpdateProfileEmptyView(visible: Boolean) {}
        fun onUpdateProfileSuccessfully(user: User) {}

    }

}