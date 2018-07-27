package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.SignUpActivity
import com.brainsocket.globalpages.di.module.SignUpModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-15.
 */
@PerActivity
@Component(modules = [SignUpModule::class])
public interface SignUpComponent {
    fun inject(signUpActivity: SignUpActivity)
}