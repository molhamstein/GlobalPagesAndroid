package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.repositories.DataStoreRepositories
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

/**
 * Created by Adhamkh on 2018-08-31.
 */
class ProfilePresenter constructor(val context: Context) : ProfileContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ProfileContract.View

    private var Index: Int = 0;

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

    override fun loadUserPosts(criteria: MutableMap<String, Pair<String, String>>) {
        view.showUserPostsProgress(true)
        ApiService().getUserPosts(ServerInfo.postUrl, criteria, object : ParsedRequestListener<MutableList<Post>> {
            override fun onResponse(response: MutableList<Post>?) {
                if ((response != null)) {
                    view.showUserPostsProgress(false)
                    if (response.size > 0)
                        view.onUserPostsListSuccessfully(response)
                    else
                        view.showUserPostsEmptyView(true)
                    return
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showUserPostsLoadErrorMessage(true)
            }
        })

    }

    override fun loadUserBusinesses(criteria: MutableMap<String, Pair<String, String>>) {
        view.showUserBusinessesProgress(true)
        ApiService().getUserBusinesses(ServerInfo.businessGuideUrl, criteria, object : ParsedRequestListener<MutableList<BusinessGuide>> {
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
            view.showUserCategoriesEmptyView(true)
            return
        }

        val categories = mutableListOf<Category>()

        val businessCategoryList = DataStoreRepositories(context).getBusinessCategories()

        businessCategoryList?.forEach {
            if (user.postCategoriesIds!!.contains(it.id)) categories.add(it)
        }

        val postCategoryList = DataStoreRepositories(context).getPostCategories()

        postCategoryList?.forEach {
            if (user.postCategoriesIds!!.contains(it.id)) categories.add(it)
        }

        view.showUserCategoriesProgress(false)
        view.onUserCategoriesListSuccessfully(categories)

    }

//    override fun loadDefaultVolume() {
//        view.showProgress(true)
//
//        var criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("limit", "1")
//            put("order", "id DESC")
//            put("skip", Index.toString())
//        }
//        loadData(criteria)
//    }
//
//    override fun loadNextVolume() {
//        view.showProgress(true)
//        Index--
//        if (Index < 0) {
//            Index = 0
//            view.noMoreData()
//        }
//        var criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("limit", "1")
//            put("order", "id DESC")
//            put("skip", Index.toString())
//        }
//        loadData(criteria)
//    }
//
//    override fun loadPreviousVolume() {
//        view.showProgress(true)
//
//        Index++
//        Index = Math.max(Index, 0)
//
//        var criteria: MutableMap<String, String> = HashMap()
//        criteria.apply {
//            put("limit", "1")
//            put("order", "id DESC")
//            put("skip", Index.toString())
//        }
//        loadData(criteria)
//    }
//
//    override fun loadVolumeById(id: String) {
////        ApiService().getVolumes(ServerInfo.VolumeUrl,)
//    }

}