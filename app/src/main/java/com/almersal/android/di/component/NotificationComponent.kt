package com.almersal.android.di.component

import com.almersal.android.activities.NotificationActivity
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [NotificationModule::class])
interface NotificationComponent {
    fun inject(notificationActivity: NotificationActivity)
}