package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.os.Handler
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.di.component.DaggerSplashComponent
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.ui.TagsCollectionContact
import com.brainsocket.globalpages.di.ui.TagsCollectionPresenter
import com.brainsocket.globalpages.utilities.intentHelper
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
            intentHelper.startMainActivity(baseContext)
            finish()
        }, Splash_Delay)

    }

}