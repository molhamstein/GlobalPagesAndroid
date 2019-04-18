package com.almersal.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.almersal.android.utilities.LocaleUtils

abstract class BaseActivity : AppCompatActivity(), LocaleUtils.LanguageListener {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        LocaleUtils.updateConfig(BaseActivity@ this)
    }


    override fun onLanguageChange() {
        this.recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBaseCreate(savedInstanceState)
    }

    abstract fun onBaseCreate(savedInstanceState: Bundle?)
}