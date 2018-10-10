package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.PostAddActivity
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.PostModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [PostModule::class, TagsCollectionModule::class, AttachmentModule::class])
interface PostAddComponent {
    fun inject(postAddActivity: PostAddActivity)
}