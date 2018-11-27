package com.almersal.android.di.component

import com.almersal.android.activities.FindNearByActivity
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-08-24.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class, BusinessGuidesModule::class])
public interface FindNearByComponent {
    fun inject(findNearByActivity: FindNearByActivity)
}