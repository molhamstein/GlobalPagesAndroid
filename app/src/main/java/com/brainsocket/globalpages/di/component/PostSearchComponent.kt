package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.PostSearchActivity
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-08-11.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class])
public interface PostSearchComponent {
    fun inject(postSearchActivity: PostSearchActivity)
}