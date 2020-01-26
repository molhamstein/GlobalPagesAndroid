package com.almersal.android.activities

import android.os.Bundle
import android.os.Handler
import com.almersal.android.R
import com.almersal.android.di.component.DaggerSplashComponent
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.NotificationContract
import com.almersal.android.di.ui.NotificationPresenter
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.utilities.IntentHelper
import javax.inject.Inject


class SplashActivity : BaseActivity(), TagsCollectionContact.View, NotificationContract.View {

    companion object {
        const val Splash_Delay: Long = 1000
    }

    @Inject
    lateinit var presenter: TagsCollectionPresenter

    @Inject
    lateinit var notificationPresenter: NotificationPresenter

    private fun initDI() {
        val component = DaggerSplashComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .notificationModule(NotificationModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadBundle(false)

        notificationPresenter.attachView(this)
        notificationPresenter.subscribe()
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splash_layout)
        val extras = intent.extras?:Bundle()

        initDI()
        Handler(mainLooper).postDelayed({
            IntentHelper.startMainActivity(baseContext,extras?: Bundle())
            finish()
        }, Splash_Delay)

    }


}