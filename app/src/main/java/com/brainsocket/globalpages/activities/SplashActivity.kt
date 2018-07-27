package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.os.Handler
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.utilities.intentHelper


class SplashActivity : BaseActivity() {

    companion object {
        const val Splash_Delay: Long = 1000
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splash_layout)

        Handler(mainLooper).postDelayed({
            intentHelper.startMainActivity(baseContext)
            finish()
        }, Splash_Delay)

    }

}