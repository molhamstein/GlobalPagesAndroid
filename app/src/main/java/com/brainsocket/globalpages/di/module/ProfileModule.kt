package com.brainsocket.globalpages.di.module

import android.app.Activity
import android.content.Context
import com.brainsocket.globalpages.di.ui.ProfilePresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-09-06.
 */
@Module
class ProfileModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getProfilePresenter(): ProfilePresenter {
        return ProfilePresenter(provideContext())
    }

}