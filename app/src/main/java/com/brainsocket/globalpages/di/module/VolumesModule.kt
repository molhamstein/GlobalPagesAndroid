package com.brainsocket.globalpages.di.module

import android.app.Activity
import android.content.Context
import com.brainsocket.globalpages.di.ui.VolumesPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-06-29.
 */
@Module
class VolumesModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getVolumesPresenter(): VolumesPresenter {
        return VolumesPresenter(provideContext())
    }

}