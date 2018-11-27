package com.almersal.android.di.component

import com.almersal.android.activities.SplashActivity
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-08-09.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class])
public interface SplashComponent {
    fun inject(splashActivity: SplashActivity)
}