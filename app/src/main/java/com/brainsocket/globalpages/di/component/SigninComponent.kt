package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.SigninActivity
import com.brainsocket.globalpages.di.module.SigninModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-15.
 */
@PerActivity
@Component(modules = [SigninModule::class])
public interface SigninComponent {
    fun inject(signinActivity: SigninActivity)
}