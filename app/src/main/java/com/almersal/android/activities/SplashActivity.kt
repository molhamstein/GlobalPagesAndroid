package com.almersal.android.activities

import android.os.Bundle
import android.os.Handler
import com.almersal.android.R
import com.almersal.android.di.component.DaggerSplashComponent
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.utilities.IntentHelper
import javax.inject.Inject


class SplashActivity : BaseActivity(), TagsCollectionContact.View {

    companion object {
        const val Splash_Delay: Long = 1000
    }

    @Inject
    lateinit var presenter: TagsCollectionPresenter

    private fun initDI() {
        val component = DaggerSplashComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadBundle(false)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splash_layout)
        initDI()
        Handler(mainLooper).postDelayed({
            IntentHelper.startMainActivity(baseContext)
            finish()
        }, Splash_Delay)

    }

}