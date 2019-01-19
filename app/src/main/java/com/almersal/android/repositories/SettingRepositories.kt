package com.almersal.android.repositories

import android.content.Context

class SettingRepositories constructor(context: Context) : Repository(context) {

    companion object {
        const val Notification_Token_Tag = "Notification_Token_Tag"
    }


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


}