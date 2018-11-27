package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.SigninPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-06-14.
 */
@Module
class SigninModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getSigninPresenter(): SigninPresenter {
        return SigninPresenter(provideContext())
    }

}