package com.almersal.android.di.component

import com.almersal.android.activities.SignUpActivity
import com.almersal.android.di.module.SignUpModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-06-15.
 */
@PerActivity
@Component(modules = [SignUpModule::class])
public interface SignUpComponent {
    fun inject(signUpActivity: SignUpActivity)
}