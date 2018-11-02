package com.brainsocket.globalpages.di.module

import android.app.Activity
import android.content.Context
import com.brainsocket.globalpages.di.ui.NotificationPresenter
import dagger.Module
import dagger.Provides

@Module
class NotificationModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getNotificationPresenter(): NotificationPresenter {
        return NotificationPresenter(provideContext())
    }


}