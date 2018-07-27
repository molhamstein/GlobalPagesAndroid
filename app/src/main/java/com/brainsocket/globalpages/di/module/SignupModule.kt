package com.brainsocket.globalpages.di.module

import android.app.Activity
import android.content.Context
import com.brainsocket.globalpages.di.ui.SignUpPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-06-15.
 */
@Module
class SignUpModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getSignUpPresenter(): SignUpPresenter {
        return SignUpPresenter(provideContext())
    }

}