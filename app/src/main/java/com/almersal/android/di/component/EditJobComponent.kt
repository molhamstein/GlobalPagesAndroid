package com.almersal.android.di.component

import com.almersal.android.activities.AddNewJobActivity
import com.almersal.android.activities.EditJobActivity
import com.almersal.android.di.module.AddNewJobModule
import com.almersal.android.di.module.EditJobModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class, EditJobModule::class])
interface EditJobComponent {
    fun inject(activity: EditJobActivity)
}