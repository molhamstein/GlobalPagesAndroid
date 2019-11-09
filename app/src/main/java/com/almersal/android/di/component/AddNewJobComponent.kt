package com.almersal.android.di.component

import com.almersal.android.activities.AddNewJobActivity
import com.almersal.android.di.module.AddNewJobModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class, AddNewJobModule::class])
interface AddNewJobComponent {
    fun inject(activity: AddNewJobActivity)
}