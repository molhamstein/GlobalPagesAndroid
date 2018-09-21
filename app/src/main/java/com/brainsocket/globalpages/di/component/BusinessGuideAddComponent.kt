package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.BusinessGuideAddActivity
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuidesModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-09-11.
 */
@PerActivity
@Component(modules = [BusinessGuidesModule::class, TagsCollectionModule::class, AttachmentModule::class])
public interface BusinessGuideAddComponent {
    fun inject(businessGuideAddActivity: BusinessGuideAddActivity)
}