package com.almersal.android.di.component

import com.almersal.android.App
import com.almersal.android.di.module.NotificationAppModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [NotificationAppModule::class])
interface AppCompoenent {
    fun inject(app: App)
}