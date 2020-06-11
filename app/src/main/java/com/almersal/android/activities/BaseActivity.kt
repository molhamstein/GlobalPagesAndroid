package com.almersal.android.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.almersal.android.utilities.LocaleUtils
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseActivity : AppCompatActivity(), LocaleUtils.LanguageListener {

    lateinit var mFirebaseAnalytics: FirebaseAnalytics

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        LocaleUtils.updateConfig(BaseActivity@ this)
    }


    override fun onLanguageChange() {
        this.recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        onBaseCreate(savedInstanceState)
    }

    abstract fun onBaseCreate(savedInstanceState: Bundle?)
}