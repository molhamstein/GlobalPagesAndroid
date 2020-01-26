package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.PostPresenter
import com.almersal.android.di.ui.ProductPresenter
import dagger.Module
import dagger.Provides

@Module
class ProductModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getPostPreseneter(): ProductPresenter {
        return ProductPresenter(provideContext())
    }
}