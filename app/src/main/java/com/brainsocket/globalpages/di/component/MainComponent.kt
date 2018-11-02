package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.MainActivity
import com.brainsocket.globalpages.di.module.NotificationModule
import com.brainsocket.globalpages.di.module.PostModule
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [VolumesModule::class, PostModule::class, NotificationModule::class])
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}