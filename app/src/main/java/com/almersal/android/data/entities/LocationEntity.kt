package com.almersal.android.data.entities

import com.almersal.android.App
//import com.fasterxml.jackson.annotation.JsonIgnore

class LocationEntity {
    var nameAr: String = ""
    var nameEn: String = ""
    var id: String = ""
    var cityId: String = ""

//    @JsonIgnore
    var isSelected: Boolean = false

    constructor()

    constructor(titleAr: String, titleEn: String, id: String) {
        this.nameAr = titleAr
        this.nameEn = titleEn
        this.id = id
    }

    fun getTitle(): String {
        return if (App.app.isArabic()) nameAr else nameEn
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocationEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}