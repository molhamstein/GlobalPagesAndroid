package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.UserResumePresenter
import com.almersal.android.di.ui.SigninPresenter
import dagger.Module
import dagger.Provides
@Module
class UserResumeModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getUserResumePresent(): UserResumePresenter {
        return UserResumePresenter(provideContext())
    }

}