package com.almersal.android.di.module

import android.app.Activity
import android.content.Context
import com.almersal.android.di.ui.BusinessGuideProductPresenter
import com.almersal.android.di.ui.ProductPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by Adhamkh on 2018-10-07.
 */
@Module
class ProductAddModule constructor(private var activity: Activity) {

    @Provides
    fun provideContext(): Context {
        return activity
    }

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun getProductPresenter(): ProductPresenter {
        return ProductPresenter(provideContext())
    }

}