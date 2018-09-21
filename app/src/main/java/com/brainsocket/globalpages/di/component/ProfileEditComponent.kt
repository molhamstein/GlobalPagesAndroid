package com.brainsocket.globalpages.di.component

import com.brainsocket.globalpages.activities.ProfileEditActivity
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.ProfileModule
import com.brainsocket.globalpages.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [ProfileModule::class, AttachmentModule::class])
public interface ProfileEditComponent {
    fun inject(profileEditActivity: ProfileEditActivity)
}