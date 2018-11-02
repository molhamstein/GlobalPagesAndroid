package com.brainsocket.globalpages.di.ui

import com.brainsocket.globalpages.data.entities.NotificationEntity


class NotificationContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadUnSeenNotifications(userId: String)
        fun loadNotifications(userId: String)
    }

    interface View : BaseContract.View {
        fun showNotificationProgress(show: Boolean) {}
        fun showNotificationLoadErrorMessage(visible: Boolean) {}
        fun showNotificationEmptyView(visible: Boolean) {}
        fun onSeenNotificationsLoaded(notificationList: MutableList<NotificationEntity>)
    }

}