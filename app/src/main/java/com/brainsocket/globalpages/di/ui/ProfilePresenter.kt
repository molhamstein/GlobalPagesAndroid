package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.data.entitiesModel.ProfileModel
import com.brainsocket.globalpages.repositories.DataStoreRepositories
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class ProfilePresenter constructor(val context: Context) : ProfileContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ProfileContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: ProfileContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun loadUserPosts(userId: String /*criteria: MutableMap<String, Pair<String, String>>*/) {
        view.showUserPostsProgress(true)
        val url = ServerInfo.postUrl + "?filter[where][ownerId]=" + userId
        ApiService().getUserPosts(url/*, criteria*/, object : ParsedRequestListener<MutableList<Post>> {
            override fun onResponse(response: MutableList<Post>?) {
                if ((response != null)) {
                    view.showUserPostsProgress(false)
                    if (response.size > 0) {
                        view.onUserPostsListSuccessfully(response)
//                        view.showUserCategoriesProgress(false)

//                        val categories: MutableList<Category> = mutableListOf()
//                        response.forEach { if (!categories.contains(it.category)) categories.add(it.category) }
//                        view.showUserCategoriesProgress(false)
//                        view.onUserCategoriesListSuccessfully(categories)
                    } else {
                        view.showUserPostsEmptyView(true)
                        view.showUserCategoriesEmptyView(true)
                    }
                    return
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showUserPostsLoadErrorMessage(true)
            }
        })

    }

    override fun loadUserBusinesses(userId: String /*criteria: MutableMap<String, Pair<String, String>>*/) {
        view.showUserBusinessesProgress(true)
        val url = ServerInfo.businessGuideUrl + "?filter[where][ownerId]=" + userId
        ApiService().getUserBusinesses(url/*, criteria*/, object : ParsedRequestListener<MutableList<BusinessGuide>> {
            override fun onResponse(response: MutableList<BusinessGuide>?) {
                if ((response != null)) {
                    view.showUserBusinessesProgress(false)
                    if (response.size > 0)
                        view.onUserBusinessesListSuccessfully(response)
                    else
                        view.showUserBusinessesEmptyView(true)
                    return
                }
                view.showUserBusinessesEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showUserBusinessesLoadErrorMessage(true)
            }
        })
    }

    override fun loadUserCategories(user: User) {
        view.showUserCategoriesProgress(true)

        if ((user.postCategoriesIds == null) or (user.postCategoriesIds!!.size <= 0)) {
            user.postCategoriesIds = Vector()
//            view.showUserCategoriesEmptyView(true)
//            return
        }

        val categories = mutableListOf<Category>()

        val businessCategoryList = DataStoreRepositories(context).getBusinessCategories()

        businessCategoryList?.forEach {
            if (it.parentCategoryId.isNullOrEmpty()) {
                if (user.postCategoriesIds!!.contains(it.id))
                    it.isSelected = true

                categories.add(it)
            }
        }

        val postCategoryList = DataStoreRepositories(context).getPostCategories()

        postCategoryList?.forEach {
            if (it.parentCategoryId.isNullOrEmpty()) {
                if (user.postCategoriesIds!!.contains(it.id))
                    it.isSelected = true

                categories.add(it)
            }
        }

        view.showUserCategoriesProgress(false)
        view.onUserCategoriesListSuccessfully(categories)

//        if ((user.postCategoriesIds == null) or (user.postCategoriesIds!!.size <= 0)) {
//            view.showUserCategoriesEmptyView(true)
//            return
//        }
//
//        val categories = mutableListOf<Category>()
//
//        val businessCategoryList = DataStoreRepositories(context).getBusinessCategories()
//
//        businessCategoryList?.forEach {
//            if (user.postCategoriesIds!!.contains(it.id)) categories.add(it)
//        }
//
//        val postCategoryList = DataStoreRepositories(context).getPostCategories()
//
//        postCategoryList?.forEach {
//            if (user.postCategoriesIds!!.contains(it.id)) categories.add(it)
//        }
//
//        view.showUserCategoriesProgress(false)
//        view.onUserCategoriesListSuccessfully(categories)

    }

    override fun updateProfile(profileModel: ProfileModel, token: String) {
        view.showUpdateProfileProgress(true)
        ApiService().updateUserProfile(ServerInfo.updateProfile + "/" + profileModel.id, profileModel, token, object : ParsedRequestListener<User> {
            override fun onResponse(response: User?) {
                view.showUpdateProfileProgress(false)
                if (response != null) {
                    view.onUpdateProfileSuccessfully(response)
                } else {
                    view.showUpdateProfileLoadErrorMessage(true)
                }
            }

            override fun onError(anError: ANError?) {
                view.showUpdateProfileLoadErrorMessage(true)
            }
        })
    }

}