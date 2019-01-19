package com.almersal.android.di.component

import com.almersal.android.activities.SplashActivity
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class, NotificationModule::class])
interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
}