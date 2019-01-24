package com.almersal.android.repositories

import android.content.Context

class SettingRepositories constructor(context: Context) : Repository(context) {

    companion object {
        const val Notification_Token_Tag = "Notification_Token_Tag"
        const val First_Subscription_Token_Tag = "First_Subscription_Token_Tag"
    }

    /*Token started*/
    fun addToken(token: String) {
        editor.putString(Notification_Token_Tag, token).apply()
    }

    fun getToken(): String? {
        val poJo: String = pref.getString(Notification_Token_Tag, "")
        if (poJo.isEmpty())
            return null
        return poJo
    }

    fun flushToken() {
        editor.putString(Notification_Token_Tag, "").apply()
    }
    /*Token ended*/


    /*First subscription started */
    fun putFirstSubscription(firstSubscription: Boolean) {
        editor.putBoolean(First_Subscription_Token_Tag, firstSubscription).apply()
    }

    fun getFirstSubscription(): Boolean {
        return pref.getBoolean(First_Subscription_Token_Tag, true)
    }
    /*First subscription ended */


}