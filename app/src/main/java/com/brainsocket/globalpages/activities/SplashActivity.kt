package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.os.Handler
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.utilities.intentHelper

/**
 * Created by Adhamkh on 2018-06-08.
 */
class SplashActivity : BaseActivity() {

    companion object {
        val Splash_Delay: Long = 1000
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.splash_layout)

        Handler(mainLooper).postDelayed(object : Runnable {
            override fun run() {
                intentHelper.startMainActivity(baseContext)
                finish()
            }
        }, Splash_Delay)

    }

}