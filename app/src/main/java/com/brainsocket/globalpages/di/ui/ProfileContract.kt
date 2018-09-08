package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entities.User

/**
 * Created by Adhamkh on 2018-08-31.
 */
class ProfileContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadUserPosts(criteria: MutableMap<String, Pair<String, String>>)
        fun loadUserBusinesses(criteria: MutableMap<String, Pair<String, String>>)
        fun loadUserCategories(user: User)
    }

    interface View : BaseContract.View {

        fun showUserCategoriesProgress(show: Boolean)
        fun showUserCategoriesLoadErrorMessage(visible: Boolean)
        fun showUserCategoriesEmptyView(visible: Boolean)
        fun onUserCategoriesListSuccessfully(categories: MutableList<Category>)


        fun showUserPostsProgress(show: Boolean)
        fun showUserPostsLoadErrorMessage(visible: Boolean)
        fun showUserPostsEmptyView(visible: Boolean)
        fun onUserPostsListSuccessfully(postList: MutableList<Post>)


        fun showUserBusinessesProgress(show: Boolean)
        fun showUserBusinessesLoadErrorMessage(visible: Boolean)
        fun showUserBusinessesEmptyView(visible: Boolean)
        fun onUserBusinessesListSuccessfully(businessGuideList: MutableList<BusinessGuide>)


    }

}