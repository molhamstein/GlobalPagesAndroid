package com.almersal.android.di.ui

import android.content.Context
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.NotificationEntity


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
        val url = ServerInfo.notificationUrl + "?filter[where][recipientId]=" + userId + "&filter[where][seen]=false"
        requestData(url = url)
    }

    override fun loadNotifications(userId: String) {
        val url = ServerInfo.notificationUrl + "?filter[where][recipientId]=" + userId //+ "&filter[where][seen]=false"
        requestData(url = url)
    }
}