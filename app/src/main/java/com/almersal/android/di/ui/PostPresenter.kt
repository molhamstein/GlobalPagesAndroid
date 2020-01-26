package com.almersal.android.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entitiesModel.PostEditModel
import com.almersal.android.data.entitiesModel.PostModel


class PostPresenter constructor(val context: Context) : PostContract.Presenter {

    private lateinit var view: PostContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun attachView(view: PostContract.View) {
        this.view = view
    }

    override fun addPost(postModel: PostModel, token: String) {
        view.showProgress(true)
        ApiService().postPost(ServerInfo.postUrl, postModel, token, object : ParsedRequestListener<Post> {
            override fun onResponse(response: Post?) {
                view.showProgress(false)
                if (response != null) {
                    view.onAddPostSuccessfully(response)
                } else {
                    view.onAddPostFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })
    }

    override fun updatePost(postModel: PostEditModel, token: String) {
        view.showProgress(true)
        ApiService().putPost(ServerInfo.postUrl, postModel, token, object : ParsedRequestListener<Post> {
            override fun onResponse(response: Post?) {
                view.showProgress(false)
                if (response != null) {
                    view.onUpdatePostSuccessfully(response)
                } else {
                    view.onUpdatePostFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })
    }

    override fun loadFeaturedPost() {
        val url = ServerInfo.postUrl + "?filter[where][isFeatured]=true" + "&filter[where][status]=activated"
        view.showFeaturedPostProgress(false)
        view.showFeaturedPostProgress(true)
        ApiService().getFeaturedPosts(url, object : ParsedRequestListener<MutableList<Post>> {
            override fun onResponse(response: MutableList<Post>?) {
                if (response != null) {
                    view.showFeaturedPostProgress(false)
                    view.onFeaturedPostLoadedSuccessfully(response)
                    return
                }
            }

            override fun onError(anError: ANError?) {
                view.showFeaturedPostLoadErrorMessage(true)
            }
        })

    }

    override fun loadPost(id: String) {
        view.showProgress(true)
        ApiService().getPost(ServerInfo.postUrl + "/" + id, object : ParsedRequestListener<Post> {
            override fun onResponse(response: Post?) {
                view.showProgress(false)
                if (response != null) {
                    view.onPostLoadedSuccessfully(response)
                } else {
                    view.showEmptyView(true)
                }
            }
            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })
    }



}