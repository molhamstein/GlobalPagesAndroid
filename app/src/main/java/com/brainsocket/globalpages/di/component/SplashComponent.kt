package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.SplashActivity
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-08-09.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class])
public interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
}