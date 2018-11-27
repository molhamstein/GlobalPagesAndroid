package com.almersal.android.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entitiesModel.ForgotPasswordModel
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject

/**
 * Created by Adhamkh on 2018-06-18.
 */
class ForgotPasswordPresenter constructor(val context: Context) : ForgotPasswordContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: ForgotPasswordContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: ForgotPasswordContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun requestPassword(forgotPasswordModel: ForgotPasswordModel) {
        view.showProgress(true)
        ApiService().postUserForgotPassword(ServerInfo.ForgotPasswordUrl, forgotPasswordModel, object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                view.showProgress(false)
                if (response != null) {
                    view.sendSuccessfully()
                } else {
                    view.requestFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showProgress(false)
                if (anError != null) {
                    when (anError.errorCode) {
                        404, 401 -> {
                            view.requestFail()
                            return
                        }
                    }
                }
                view.showLoadErrorMessage(true)
            }
        })

    }

}