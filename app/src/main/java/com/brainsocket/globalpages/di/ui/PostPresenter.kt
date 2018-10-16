package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entitiesModel.PostEditModel
import com.brainsocket.globalpages.data.entitiesModel.PostModel
import com.brainsocket.globalpages.repositories.UserRepository
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adhamkh on 2018-10-09.
 */
class PostPresenter constructor(val context: Context) : PostContract.Presenter {

    private val subscriptions = CompositeDisposable()
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
        val url = ServerInfo.postUrl + "?filter[where][isFeatured]=true"
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
}