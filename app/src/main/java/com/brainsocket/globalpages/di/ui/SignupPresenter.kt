package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.data.entitiesModel.SignupModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adhamkh on 2018-06-15.
 */
class SignupPresenter constructor(val context: Context) : SignupContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SignupContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: SignupContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun trySignUp() {
        if (view.checkValidation())
            signUp(view.getUser())
    }

    override fun signUp(signupModel: SignupModel) {
        view.showProgress(true)
        ApiService().postUserSignUp(ServerInfo.SignupUrl, signupModel, object : ParsedRequestListener<User> {
            override fun onResponse(response: User?) {
                if (response != null) {
                    view.NavigateAfterSignUp(response)
//                    if (view.isBusiness(response))
//                        view.signup2Business(response)
//                    else
//                        view.signupSuccesfully(response)
                    Log.v("", "")
                } else {
                    view.signupFail()
                }
            }

            override fun onError(anError: ANError?) {
                view.showProgress(false)
                if (anError != null) {
                    when (anError.errorCode) {
                        422 -> {
                            var isDuplicateEmail: Boolean = anError.errorBody.contains("email", true)
                            var isDuplicateUserName: Boolean = anError.errorBody.contains("username", true)
                            view.signupduplicate(DuplicateModel(isDuplicateEmail, isDuplicateUserName))
                            return
                        }
                    }
                }
                view.showLoadErrorMessage(true)
                Log.v("", "")
            }
        })
    }

}
    
