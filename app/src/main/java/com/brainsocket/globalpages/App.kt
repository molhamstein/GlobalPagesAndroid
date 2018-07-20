package com.brainsocket.globalpages

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.jacksonandroidnetworking.JacksonParserFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 * Created by Adhamkh on 2018-06-08.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

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
                .build()

        )
    }

}