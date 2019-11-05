package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.ForgotPasswordPresenter
import com.almersal.android.di.ui.JobSearchPresenter
import dagger.Module
import dagger.Provides

@Module
class JobSearchModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getJobSearchPresenter(): JobSearchPresenter {
        return JobSearchPresenter(provideContext())
    }

}