package com.almersal.android.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.NotificationEntity
import com.almersal.android.data.entitiesResponses.NotificationReadedResponse
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.interfaces.StringRequestListener


class NotificationPresenter constructor(val context: Context) : NotificationContract.Presenter {

    private lateinit var view: NotificationContract.View

    init {

    }

    override fun subscribe() {

    }

    override fun unSubscribe() {

    }

    override fun attachView(view: NotificationContract.View) {
        this.view = view
    }

    private fun requestData(url: String) {
        view.showNotificationProgress(true)
        ApiService().getNotifications(url = url, requestListener = object : ParsedRequestListener<MutableList<NotificationEntity>> {
            override fun onResponse(response: MutableList<NotificationEntity>?) {
                view.showNotificationProgress(false)
                if ((response != null)) {
                    if (response.size > 0) {
                        view.onSeenNotificationsLoaded(response)
                        return
                    }
                }
                view.showNotificationEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showNotificationLoadErrorMessage(true)
            }
        })
    }

    override fun loadUnSeenNotifications(userId: String) {
        val url = ServerInfo.notificationUrl + "?filter[where][recipientId]=" + userId + "&filter[where][seen]=false&filter[order]=creationDate%20DESC"
        requestData(url = url)
    }

    override fun loadNotifications(userId: String) {
        val url = ServerInfo.notificationUrl + "?filter[where][recipientId]=" + userId + "&filter[order]=creationDate%20DESC" //+ "&filter[where][seen]=false"
        requestData(url = url)
    }

    override fun loadNotifications() {
        val url = ServerInfo.notificationUrl + "?filter[order]=creationDate%20DESC"
        requestData(url = url)
    }

    override fun setNotificationSeen(notificationIds: MutableList<String>) {
        val url = ServerInfo.notificationSeenUrl
        val token = UserRepository(context).getUser()!!.token
        ApiService().setNotificationsSeen(url = url, notificationIds = notificationIds, token = token,
                requestListener = object : ParsedRequestListener<NotificationReadedResponse> {
                    override fun onResponse(response: NotificationReadedResponse?) {
                        view.onNotificationSetSeenSuccessfully()
                    }

                    override fun onError(anError: ANError?) {
                        view.onNotificationSetSeenFailed()
                    }
                })
    }

    override fun registerFireBaseToken(fireBaseToken: String, token: String) {

        ApiService().putFireBaseToken(url = ServerInfo.fireBaseNotificationUrl, fireBaseToken = fireBaseToken, token = token
                , requestListener = object : StringRequestListener {
            override fun onResponse(response: String?) {
                view.showNotificationProgress(false)
                if ((response != null)) {
                    view.onRegisterFireBaseTokenSuccessfully()
//                    if (response.size > 0) {
//                        view.onSeenNotificationsLoaded(response)
//                        return
//                    }
                    return
                }
                view.onRegisterFireBaseTokenFailed()
            }


            override fun onError(anError: ANError?) {
                view.onRegisterFireBaseTokenFailed()
            }
        })

    }


}