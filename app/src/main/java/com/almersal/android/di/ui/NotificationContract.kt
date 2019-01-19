package com.almersal.android.di.ui

import com.almersal.android.data.entities.NotificationEntity


class NotificationContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadUnSeenNotifications(userId: String)
        fun loadNotifications(userId: String)

        fun registerFireBaseToken(fireBaseToken: String, token: String)
    }

    interface View : BaseContract.View {
        fun showNotificationProgress(show: Boolean) {}
        fun showNotificationLoadErrorMessage(visible: Boolean) {}
        fun showNotificationEmptyView(visible: Boolean) {}
        fun onSeenNotificationsLoaded(notificationList: MutableList<NotificationEntity>) {}

        fun onRegisterFireBaseTokenSuccessfully() {}
        fun onRegisterFireBaseTokenFailed() {}
    }

}