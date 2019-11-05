package com.almersal.android.di.component

import com.almersal.android.activities.UserResumeActivity
import com.almersal.android.di.module.UserResumeModule
import com.almersal.android.di.scope.PerActivity
import dagger.Component

@PerActivity
@Component(modules = [UserResumeModule::class])
interface UserResumeComponent {
    fun inject(userResumeActivity: UserResumeActivity)
}