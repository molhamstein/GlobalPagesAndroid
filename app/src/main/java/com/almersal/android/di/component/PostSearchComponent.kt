package com.almersal.android.di.component

import com.almersal.android.activities.PostSearchActivity
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-08-11.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class])
public interface PostSearchComponent {
    fun inject(postSearchActivity: PostSearchActivity)
}