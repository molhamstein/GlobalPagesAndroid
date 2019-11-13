package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.ApplicantsPresenter
import com.almersal.android.di.ui.JobDetailsPresenter
import dagger.Module
import dagger.Provides

@Module
class ApplicantsModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getApplicantsPresenter(): ApplicantsPresenter {
        return ApplicantsPresenter(provideContext())
    }

}