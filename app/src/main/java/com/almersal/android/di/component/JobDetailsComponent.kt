package com.almersal.android.di.component

import com.almersal.android.activities.JobDetailsActivity
import com.almersal.android.activities.JobsSearchActivity
import com.almersal.android.di.module.JobDetailsModule
import com.almersal.android.di.module.JobSearchModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [JobDetailsModule::class])
interface JobDetailsComponent {
    fun inject(jobDetailsActivity: JobDetailsActivity)
}