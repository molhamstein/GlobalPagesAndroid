package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.NotificationActivity
import com.brainsocket.globalpages.di.module.NotificationModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [NotificationModule::class])
interface NotificationComponent {
    fun inject(notificationActivity: NotificationActivity)
}