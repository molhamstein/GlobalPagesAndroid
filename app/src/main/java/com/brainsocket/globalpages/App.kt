package com.brainsocket.globalpages

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.brainsocket.globalpages.utilities.LocaleUtils
import com.jacksonandroidnetworking.JacksonParserFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.util.*


/**
 * Created by Adhamkh on 2018-06-08.
 */
class App : MultiDexApplication() {

    companion object {
        var systemLanguage: String = ""
        lateinit var app: App;
    }


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        app = this
//        val okHttpClient = OkHttpClient().newBuilder()
//                .connectTimeout(600, TimeUnit.SECONDS)
//                .readTimeout(600, TimeUnit.SECONDS)
//                .addNetworkInterceptor()
//                .build()
//        AndroidNetworking.initialize(applicationContext, okHttpClient)
        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.setParserFactory(JacksonParserFactory())

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())

        var locale = "ar"//DataStore . getInstance ().getLocale();
        systemLanguage = "ar"
        if (!locale.isEmpty()) {
            LocaleUtils.setLocale(Locale(locale))
            LocaleUtils.updateConfig(this, resources.configuration)
        }

    }

    fun getSystemLanguage(): String {
        return systemLanguage
    }

    fun isArabic(): Boolean {
        val locale = systemLanguage
        return if (locale !== "" && locale != null) locale!!.equals("ar", ignoreCase = true)
        else getSystemLanguage().equals("ar", false)
    }

}