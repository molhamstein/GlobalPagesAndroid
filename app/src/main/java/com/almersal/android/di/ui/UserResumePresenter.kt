package com.almersal.android.di.ui

import android.content.Context
import android.util.Log
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class UserResumePresenter(val context: Context) : UserResumeContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: UserResumeContract.View


    init {

    }

    override fun subscribe() {

    }

    override fun attachView(view: UserResumeContract.View) {
        this.view = view
    }

    override fun unSubscribe() {
        subscriptions.clear()
    }

    override fun getUserResume(userId: String) {
        view.showProgress(true)
        ApiService().getUserCV(
            ServerInfo.getUserResume + userId + "?filter={\"include\":[\"CV\"]}",
            object : ParsedRequestListener<User> {
                override fun onResponse(response: User?) {
                    view.showProgress(false)
                    if (response != null) {
                        val token = UserRepository(context).getUser()!!.token
                        response.token = token
                        UserRepository(context = context).addUser(response)
                        view.updateUserInfo(response)
                    } else {
                        view.getFailed()
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
////                view.getFailed()
//                if (anError != null) {
//                    when (anError.errorCode) {
////                        401 -> {
////                            view.loginFail()
////                            return
////                        }
//                    }
//                }
                    view.showLoadErrorMessage(true)
                }
            })
    }




}