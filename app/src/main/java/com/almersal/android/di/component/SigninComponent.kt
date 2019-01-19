package com.almersal.android.di.component

import com.almersal.android.activities.SignInActivity
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.SigninModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [SigninModule::class, NotificationModule::class])
interface SigninComponent {
    fun inject(signInActivity: SignInActivity)
}