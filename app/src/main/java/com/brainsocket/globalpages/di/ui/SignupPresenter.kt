package com.brainsocket.globalpages.di.ui

import android.content.Context
import android.util.Log
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.api.ApiService
import com.brainsocket.globalpages.api.ServerInfo
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesModel.DuplicateModel
import com.brainsocket.globalpages.data.entitiesModel.SignUpModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Adhamkh on 2018-06-15.
 */
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
                            var isDuplicateEmail: Boolean = anError.errorBody.contains("email", true)
                            var isDuplicateUserName: Boolean = anError.errorBody.contains("username", true)
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

}
    
