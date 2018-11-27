package com.almersal.android.repositories

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager

/**
 * Created by Adhamkh on 2018-08-08.
 */
abstract class Repository constructor(var context: Context) {

    var pref: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        editor = pref.edit()
    }

}