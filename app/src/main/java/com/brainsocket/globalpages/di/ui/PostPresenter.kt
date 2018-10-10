package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
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
        ApiService().postPost(ServerInfo.postUrl, postModel, token, object : StringRequestListener {
            override fun onResponse(response: String?) {
                view.showProgress(false)
                view.onAddPostSuccessfully()
            }

            override fun onError(anError: ANError?) {
                view.showLoadErrorMessage(true)
            }
        })
    }
}