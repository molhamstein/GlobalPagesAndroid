package com.almersal.android.di.module

import android.app.Activity
import android.app.Application
import android.content.Context
import com.almersal.android.di.ui.NotificationPresenter
import dagger.Module
import dagger.Provides


@Module
class NotificationAppModule constructor(private var app: Application) {

    @Provides
    fun provideContext(): Context {
        return app.baseContext
    }

    @Provides
    fun provideApplication(): Application {
        return app
    }

    @Provides
    fun getNotificationPresenter(): NotificationPresenter {
        return NotificationPresenter(provideContext())
    }


}