package com.almersal.android.di.component

import com.almersal.android.activities.SignInActivity
import com.almersal.android.di.module.SigninModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-15.
 */
@PerActivity
@Component(modules = [SigninModule::class])
public interface SigninComponent {
    fun inject(signInActivity: SignInActivity)
}