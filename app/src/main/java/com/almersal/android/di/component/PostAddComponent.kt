package com.almersal.android.di.component

import com.almersal.android.activities.PostAddActivity
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [PostModule::class, TagsCollectionModule::class, AttachmentModule::class])
interface PostAddComponent {
    fun inject(postAddActivity: PostAddActivity)
}