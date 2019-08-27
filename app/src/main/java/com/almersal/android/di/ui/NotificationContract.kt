package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.data.entities.NotificationEntity


class NotificationContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun loadUnSeenNotifications(userId: String)

        fun loadNotifications(userId: String, limit: Int, skip: Int)

        fun loadNotifications()

        fun clearAllNotifications(context: Context)

        fun deleteNotificationById(notificationId: String)

        fun setNotificationSeen(notificationIds: MutableList<String>)

        fun setNotificationClicked(notification: NotificationEntity)

        fun registerFireBaseToken(fireBaseToken: String, token: String)
    }

    interface View : BaseContract.View {
        fun showNotificationProgress(show: Boolean) {}
        fun showNotificationLoadErrorMessage(visible: Boolean) {}
        fun showNotificationEmptyView(visible: Boolean) {}
        fun onSeenNotificationsLoaded(notificationList: MutableList<NotificationEntity>) {}

        fun onNotificationSetSeenSuccessfully() {}
        fun onNotificationSetSeenFailed() {}

        fun onNotificationSetClickedSuccessfully() {}

        fun onRegisterFireBaseTokenSuccessfully() {}
        fun onRegisterFireBaseTokenFailed() {}

        fun onNotificationsDeleteProgress(show: Boolean) {}
        fun onNotificationsClearedSuccessfully() {}
        fun onNotificationsClearFailed() {}

        fun onNotificationDeleteSuccessfully() {}
        fun onNotificationDeleteFailed() {}

    }

}