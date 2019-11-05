package com.almersal.android.data.entities

import com.almersal.android.App

data class JobBusiness(
    val city: City? = null,
    val cityId: String? = null,
    val covers: List<Any>? = null,
    val deleted: Boolean? = null,
    val id: String? = null,
    val location: Location? = null,
    val locationId: String? = null,
    val nameAr: String? = null,
    val nameEn: String? = null,
    val nameUnique: String? = null,
    val status: String? = null,
    val logo: String? = null
) {


    val name: String?
        get() {
            return if (App.app.isArabic()) nameAr else nameEn
        }
}