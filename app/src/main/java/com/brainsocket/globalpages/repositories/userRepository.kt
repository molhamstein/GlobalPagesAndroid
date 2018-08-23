package com.brainsocket.globalpages.repositories

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import com.brainsocket.globalpages.data.entities.User
import com.google.gson.Gson

/**
 * Created by Adhamkh on 2018-06-30.
 */
class userRepository constructor( context: Context):Repository(context)  {

    companion object {
        var USER_TAG: String = "USER"
    }

    fun addUser(user: User) {
        editor.putString(USER_TAG, Gson().toJson(user)).apply()
    }

    fun getUser(): User? {
        var user: User? = null
        var pojo = pref.getString(USER_TAG, "")
        if (pojo.isNotEmpty())
            user = Gson().fromJson(pojo, User::class.java)
        return user
    }

    fun flushUser() {
        editor.putString(USER_TAG, "")
    }


}