package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.SignupActivity
import com.brainsocket.globalpages.di.module.SignupModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-15.
 */
@PerActivity
@Component(modules = [SignupModule::class])
public interface SignupComponent {
    fun inject(signupActivity: SignupActivity)
}