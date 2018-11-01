package com.brainsocket.globalpages.repositories

import android.content.Context
import com.brainsocket.globalpages.data.entities.User
import com.google.gson.Gson


class UserRepository constructor(context: Context) : Repository(context) {

    companion object {
        var USER_TAG: String = "USER"
    }

    fun addUser(user: User) {
        editor.putString(USER_TAG, Gson().toJson(user)).apply()
    }

    fun getUser(): User? {
        var user: User? = null
        val poJo = pref.getString(USER_TAG, "")
        if (poJo.isNotEmpty())
            user = Gson().fromJson(poJo, User::class.java)
        return user
    }

    fun flushUser() {
        editor.putString(USER_TAG, "").apply()
    }


}