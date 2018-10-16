package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.MainActivity
import com.brainsocket.globalpages.di.module.PostModule
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-29.
 */

@PerActivity
@Component(modules = [VolumesModule::class, PostModule::class])
interface MainComponent {
    fun inject(mainActivity: MainActivity)
}