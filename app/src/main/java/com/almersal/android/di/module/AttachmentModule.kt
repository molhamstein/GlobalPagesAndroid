package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.AttachmentPresenter
import com.almersal.android.di.ui.VolumesPresenter
import dagger.Module
import dagger.Provides

@Module
class AttachmentModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getAttachmentPresenter(): AttachmentPresenter {
        return AttachmentPresenter(provideContext())
    }


}