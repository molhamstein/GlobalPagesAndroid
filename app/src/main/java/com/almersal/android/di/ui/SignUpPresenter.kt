package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesModel.DuplicateModel
import com.almersal.android.data.entitiesModel.LoginModel
import com.almersal.android.data.entitiesModel.SignUpModel
import com.almersal.android.data.entitiesResponses.LoginResponse
import io.reactivex.disposables.CompositeDisposable

class SignUpPresenter constructor(val context: Context) : SignUpContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SignUpContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: SignUpContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun trySignUp() {
        if (view.checkValidation())
            signUp(view.getUser())
    }

    override fun signUp(signUpModel: SignUpModel) {
        view.showProgress(true)
        ApiService().postUserSignUp(ServerInfo.SignUpUrl, signUpModel, object : ParsedRequestListener<User> {
            override fun onResponse(response: User?) {
                if (response != null) {
                    view.showProgress(false)
                    view.navigateAfterSignUp(response)
//                    if (view.isBusiness(response))
//                        view.signUp2Business(response)
//                    else
//                        view.signUpSuccessfully(response)
                    Log.v("", "")
                } else {
                    view.signUpFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showProgress(false)
                if (anError != null) {
                    when (anError.errorCode) {
                        422 -> {
                            val isDuplicateEmail: Boolean = anError.errorBody.contains("email", true)
                            val isDuplicateUserName: Boolean = anError.errorBody.contains("username", true)
                            view.signUpDuplicate(DuplicateModel(isDuplicateEmail, isDuplicateUserName))
                            return
                        }
                    }
                }
                view.showLoadErrorMessage(true)
                Log.v("", "")
            }
        })
    }
    override fun authenticate(loginModel: LoginModel) {
        view.showProgress(true)
        ApiService().postUserLogin(ServerInfo.SigninUrl, loginModel, object : ParsedRequestListener<LoginResponse> {
            override fun onResponse(response: LoginResponse?) {
                view.showProgress(false)
                if (response != null) {
                    view.loginSuccessfully(response)
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
    
