package com.almersal.android.data.entities

import com.almersal.android.App

data class Location(
    val deleted: Boolean,
    val id: String,
    val nameAr: String,
    val nameEn: String
) {
    fun getTitle(): String {
        return if (App.app.isArabic()) nameAr else nameEn
    }

}