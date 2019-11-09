package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.EditJobPresenter
import dagger.Module
import dagger.Provides

@Module
class EditJobModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getPresenter(): EditJobPresenter {
        return EditJobPresenter(provideContext())
    }

}