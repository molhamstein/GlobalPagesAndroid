package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.ForgotPasswordActivity
import com.brainsocket.globalpages.di.module.ForgotPasswordModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-18.
 */
@PerActivity
@Component(modules = [ForgotPasswordModule::class])
public interface ForgotPasswordComponent {
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
}