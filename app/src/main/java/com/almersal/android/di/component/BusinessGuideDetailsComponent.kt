package com.almersal.android.di.component

import com.almersal.android.activities.BusinessGuideDetailsActivity
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component


@PerActivity
@Component(modules = [BusinessGuidesModule::class])
interface BusinessGuideDetailsComponent {
    fun inject(businessGuideDetailsActivity: BusinessGuideDetailsActivity)
}