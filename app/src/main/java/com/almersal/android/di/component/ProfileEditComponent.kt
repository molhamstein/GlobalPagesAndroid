package com.almersal.android.di.component

import com.almersal.android.activities.ProfileEditActivity
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [ProfileModule::class, AttachmentModule::class])
public interface ProfileEditComponent {
    fun inject(profileEditActivity: ProfileEditActivity)
}