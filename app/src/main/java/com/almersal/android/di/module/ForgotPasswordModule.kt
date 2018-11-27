package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.ForgotPasswordPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-06-18.
 */
@Module
class ForgotPasswordModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getForgotPasswordPresenter(): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(provideContext())
    }

}