package com.almersal.android

import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.almersal.android.di.component.DaggerAppCompoenent
import com.almersal.android.di.module.NotificationAppModule
import com.almersal.android.di.ui.NotificationContract
import com.almersal.android.di.ui.NotificationPresenter
import com.almersal.android.repositories.SettingData
import com.almersal.android.repositories.SettingRepositories
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.gsonparserfactory.GsonParserFactory
import com.almersal.android.utilities.LocaleUtils
import com.crashlytics.android.Crashlytics
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
//import com.onesignal.OneSignal
//import com.jacksonandroidnetworking.JacksonParserFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.util.*
import javax.inject.Inject

class App : MultiDexApplication(), NotificationContract.View {

    companion object {
        var systemLanguage: String = ""
        lateinit var app: App
    }

    @Inject
    lateinit var notificationPresenter: NotificationPresenter

    fun initDI() {
        val component = DaggerAppCompoenent.builder()
                .notificationAppModule(NotificationAppModule(this))
                .build()
        component.inject(this)

        notificationPresenter.attachView(this)
        notificationPresenter.subscribe()
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        app = this
        initDI()

        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.setParserFactory(GsonParserFactory())

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

        if (!BuildConfig.DEBUG)
            Fabric.with(this, Crashlytics())

        FirebaseApp.initializeApp(this)
        val messages = FirebaseMessaging.getInstance()
        messages.subscribeToTopic(SettingData.generalTopic)
        messages.isAutoInitEnabled = true
        val user = UserRepository(context = baseContext).getUser()
        if (user != null) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {

                override fun onComplete(p0: Task<InstanceIdResult>) {
                    if (p0.isSuccessful) {
                        val token: String? = p0.result?.token
                        if ((token != null)) {
                            SettingRepositories(applicationContext).addToken(token)
                            notificationPresenter.registerFireBaseToken(fireBaseToken = token, token = user.token)
                        }
                    } else {
                        Log.v("AAA", "")
                    }
//                this.sendRegistrationToServer(token)
                }
            })
        }

    }

    fun isArabic(): Boolean = systemLanguage.equals("ar", false)


    /*Notification presenter started*/


    /*Notification presenter ended*/

}

