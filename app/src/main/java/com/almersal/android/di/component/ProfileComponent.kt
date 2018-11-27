package com.almersal.android.di.component

import com.almersal.android.activities.ProfileActivity
import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-09-06.
 */
@PerActivity
@Component(modules = [ProfileModule::class])
public interface ProfileComponent {
    fun inject(profileActivity: ProfileActivity)
}