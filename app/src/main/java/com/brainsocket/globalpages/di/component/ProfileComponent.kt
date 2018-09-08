package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.ProfileActivity
import com.brainsocket.globalpages.di.module.ProfileModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-09-06.
 */
@PerActivity
@Component(modules = [ProfileModule::class])
public interface ProfileComponent {
    fun inject(profileActivity: ProfileActivity)
}