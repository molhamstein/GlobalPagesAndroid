package com.almersal.android.di.component

import com.almersal.android.activities.PostDetailsActivity
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [PostModule::class])
interface PostDetailsComponent {
    fun inject(postDetailsActivity: PostDetailsActivity)
}