package com.almersal.android.di.component

import com.almersal.android.activities.SignUpActivity
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.SignUpModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [SignUpModule::class, NotificationModule::class])
interface SignUpComponent {
    fun inject(signUpActivity: SignUpActivity)
}