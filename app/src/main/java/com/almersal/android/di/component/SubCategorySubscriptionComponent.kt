package com.almersal.android.di.component

import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.scope.PerActivity
import com.almersal.android.dialogs.bottomSheetFragments.SubCategorySubscriptionBottomSheet
import dagger.Component

@PerActivity
@Component(modules = [ProfileModule::class, TagsCollectionModule::class])
interface SubCategorySubscriptionComponent {
    fun inject(subCategorySubscription: SubCategorySubscriptionBottomSheet)
}