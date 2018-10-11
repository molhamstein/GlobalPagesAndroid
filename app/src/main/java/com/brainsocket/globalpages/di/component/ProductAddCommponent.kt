package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.ProductAddActivity
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuideProductModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [BusinessGuideProductModule::class, TagsCollectionModule::class, AttachmentModule::class])
interface ProductAddCommponent {
    fun inject(productAddActivity: ProductAddActivity)
}