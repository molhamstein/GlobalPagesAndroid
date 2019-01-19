package com.almersal.android

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.gsonparserfactory.GsonParserFactory
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.almersal.android.utilities.LocaleUtils
import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
//import com.onesignal.OneSignal
//import com.jacksonandroidnetworking.JacksonParserFactory
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
        AndroidNetworking.setParserFactory(GsonParserFactory())
//        AndroidNetworking.enableLogging()
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY)
//        AndroidNetworking.setParserFactory(JacksonParserFactory())

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

//        OneSignal Initialization
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init()

        FirebaseApp.initializeApp(this)
        Fabric.with(this, Crashlytics())
        val messages = FirebaseMessaging.getInstance()
        messages.isAutoInitEnabled = true


    }

    fun isArabic(): Boolean = systemLanguage.equals("ar", false)

}

