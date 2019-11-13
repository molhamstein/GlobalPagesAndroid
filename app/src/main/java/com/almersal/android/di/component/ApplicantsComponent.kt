package com.almersal.android.di.component

import com.almersal.android.activities.ApplicantsActivity
import com.almersal.android.activities.JobDetailsActivity
import com.almersal.android.di.module.ApplicantsModule
import com.almersal.android.di.module.JobDetailsModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [ApplicantsModule::class])
interface ApplicantsComponent {
    fun inject(activity: ApplicantsActivity)
}