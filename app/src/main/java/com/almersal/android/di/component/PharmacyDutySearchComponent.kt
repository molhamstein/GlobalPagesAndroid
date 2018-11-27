package com.almersal.android.di.component

import com.almersal.android.activities.PharmacyDutySearchActivity
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-10-06.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class, BusinessGuidesModule::class])
public interface PharmacyDutySearchComponent {
    fun inject(pharmacyDutySearchActivity: PharmacyDutySearchActivity)
}