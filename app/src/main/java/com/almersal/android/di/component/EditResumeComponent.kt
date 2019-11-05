package com.almersal.android.di.component

import com.almersal.android.activities.EditResumeActivity
import com.almersal.android.activities.UserResumeActivity
import com.almersal.android.di.module.EditUserResumeModule
import com.almersal.android.di.module.UserResumeModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [EditUserResumeModule::class])
interface EditResumeComponent {
    fun inject(editResumeActivity: EditResumeActivity)
}