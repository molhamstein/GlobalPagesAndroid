package com.brainsocket.globalpages.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.brainsocket.globalpages.utilities.LocaleUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity : AppCompatActivity(), LocaleUtils.LanguageListener {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        LocaleUtils.updateConfig(BaseActivity@ this)
    }


    override fun onLanguageChange() {
        this.recreate()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBaseCreate(savedInstanceState)
    }

    abstract fun onBaseCreate(savedInstanceState: Bundle?)
}