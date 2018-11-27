package com.almersal.android.di.component

import com.almersal.android.activities.ProductAddActivity
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.BusinessGuideProductModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [BusinessGuideProductModule::class, TagsCollectionModule::class, AttachmentModule::class])
interface ProductAddCommponent {
    fun inject(productAddActivity: ProductAddActivity)
}