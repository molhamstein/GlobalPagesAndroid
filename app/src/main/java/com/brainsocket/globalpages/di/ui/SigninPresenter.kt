package com.brainsocket.globalpages.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.data.entitiesModel.LoginModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adhamkh on 2018-06-14.
 */
class SigninPresenter constructor(val context: Context) : SigninContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: SigninContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: SigninContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun authenticate(loginModel: LoginModel) {
        view.showProgress(true)
        ApiService().postUserLogin(ServerInfo.SigninUrl, loginModel, object : ParsedRequestListener<LoginResponse> {
            override fun onResponse(response: LoginResponse?) {
                view.showProgress(false)
                if (response != null) {
                    view.loginSuccesfully(response)
                } else {
                    view.loginFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showProgress(false)
                if (anError != null) {
                    when (anError.errorCode) {
                        401 -> {
                            view.loginFail()
                            return
                        }
                    }
                }
                view.showLoadErrorMessage(true)
            }
        })
    }

}