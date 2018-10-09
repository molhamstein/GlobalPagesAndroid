package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.ProductManageActivity
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuideProductModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

/**
 * Created by Adhamkh on 2018-10-07.
 */
@PerActivity
@Component(modules = [BusinessGuideProductModule::class, AttachmentModule::class])
public interface ProductManageComponent {
    fun inject(productManageActivity: ProductManageActivity)
}