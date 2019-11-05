package com.almersal.android.di.component

import com.almersal.android.activities.JobsSearchActivity
import com.almersal.android.di.module.EditUserResumeModule
import com.almersal.android.di.module.JobSearchModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [TagsCollectionModule::class,JobSearchModule::class])
interface JobSearchComponenet {
    fun inject(jobsSearchActivity: JobsSearchActivity)
}