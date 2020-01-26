package com.almersal.android.di.component

import com.almersal.android.activities.ProductAddActivity
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.ProductAddModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [ProductAddModule::class, TagsCollectionModule::class, AttachmentModule::class])
interface ProductAddCommponent {
    fun inject(productAddActivity: ProductAddActivity)
}