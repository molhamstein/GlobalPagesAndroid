package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.PostPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-10-09.
 */
@Module
class PostModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getPostPreseneter(): PostPresenter {
        return PostPresenter(provideContext())
    }

}