package com.almersal.android.di.component

import com.almersal.android.activities.MainActivity
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.module.VolumesModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [VolumesModule::class, PostModule::class, NotificationModule::class])
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}