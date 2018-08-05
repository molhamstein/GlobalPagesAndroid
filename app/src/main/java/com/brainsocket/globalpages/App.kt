package com.brainsocket.globalpages

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.brainsocket.globalpages.utilities.LocaleUtils
import com.jacksonandroidnetworking.JacksonParserFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.util.*

class App : MultiDexApplication() {

    companion object {
        var systemLanguage: String = ""
        lateinit var app: App
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


        val locale = Locale.getDefault()
        var localeCode = "ar"
        if (locale.displayName.contains("en", true)) {
            localeCode = "en"
        }
        systemLanguage = localeCode
        if (!localeCode.isEmpty()) {
            LocaleUtils.setLocale(Locale(localeCode))
            LocaleUtils.updateConfig(this, resources.configuration)
        }

    }

    fun isArabic(): Boolean = systemLanguage.equals("ar", false)

}