package com.almersal.android.di.component

import com.almersal.android.activities.VolumesActivity
import com.almersal.android.di.module.VolumesModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [VolumesModule::class])
interface VolumesComponent {
    fun inject(volumesActivity: VolumesActivity)
}