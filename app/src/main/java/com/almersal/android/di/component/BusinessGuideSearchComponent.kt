package com.almersal.android.di.component

import com.almersal.android.activities.BusinessGuideSearchActivity
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class, BusinessGuidesModule::class])
public interface BusinessGuideSearchComponent {
    fun inject(businessGuideSearchActivity: BusinessGuideSearchActivity)
}