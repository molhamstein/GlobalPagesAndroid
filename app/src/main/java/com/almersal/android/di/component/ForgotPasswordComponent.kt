package com.almersal.android.di.component

import com.almersal.android.activities.ForgotPasswordActivity
import com.almersal.android.di.module.ForgotPasswordModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-18.
 */
@PerActivity
@Component(modules = [ForgotPasswordModule::class])
public interface ForgotPasswordComponent {
    fun inject(forgotPasswordActivity: ForgotPasswordActivity)
}