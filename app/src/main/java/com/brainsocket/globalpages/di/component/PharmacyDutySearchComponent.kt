package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.PharmacyDutySearchActivity
import com.brainsocket.globalpages.di.module.BusinessGuidesModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-10-06.
 */
@PerActivity
@Component(modules = [TagsCollectionModule::class, BusinessGuidesModule::class])
public interface PharmacyDutySearchComponent {
    fun inject(pharmacyDutySearchActivity: PharmacyDutySearchActivity)
}