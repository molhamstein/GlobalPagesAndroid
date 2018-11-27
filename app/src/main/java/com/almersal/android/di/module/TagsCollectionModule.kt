package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.SignUpPresenter
import com.almersal.android.di.ui.TagsCollectionPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-08-09.
 */
@Module
class TagsCollectionModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getTagsCollectionPresenter(): TagsCollectionPresenter {
        return TagsCollectionPresenter(provideContext())
    }

}