package com.almersal.android.di.component

import com.almersal.android.activities.BusinessGuideAddActivity
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [BusinessGuidesModule::class, TagsCollectionModule::class, AttachmentModule::class])
 interface BusinessGuideAddComponent {
    fun inject(businessGuideAddActivity: BusinessGuideAddActivity)
}