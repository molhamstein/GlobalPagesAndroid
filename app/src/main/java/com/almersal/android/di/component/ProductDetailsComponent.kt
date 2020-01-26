package com.almersal.android.di.component

import com.almersal.android.activities.ProductDetailsActivity
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.module.ProductModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [ProductModule::class])
interface ProductDetailsComponent {
    fun inject(postDetailsActivity: ProductDetailsActivity)
}