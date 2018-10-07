package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.BusinessGuideSearchActivity
import com.brainsocket.globalpages.di.module.BusinessGuidesModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class, BusinessGuidesModule::class])
public interface BusinessGuideSearchComponent {
    fun inject(businessGuideSearchActivity: BusinessGuideSearchActivity)
}