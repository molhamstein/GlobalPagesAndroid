package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.BusinessGuidesPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-08-24.
 */
@Module
class BusinessGuidesModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getBusinessGuidesPresenter(): BusinessGuidesPresenter {
        return BusinessGuidesPresenter(provideContext())
    }

}