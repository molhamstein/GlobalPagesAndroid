package com.brainsocket.globalpages.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.brainsocket.globalpages.utilities.LocaleUtils
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by Adhamkh on 2018-06-08.
 */
abstract class BaseActivity : AppCompatActivity(), LocaleUtils.LanguageListener {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        LocaleUtils.updateConfig(this)
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