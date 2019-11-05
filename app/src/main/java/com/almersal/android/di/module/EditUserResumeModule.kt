package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.EditResumePresenter
import com.almersal.android.di.ui.UserResumePresenter
import dagger.Module
import dagger.Provides

@Module
class EditUserResumeModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getUserResumePresent(): EditResumePresenter {
        return EditResumePresenter(provideContext())
    }

}