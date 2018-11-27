package com.almersal.android.data.entities

import com.almersal.android.App

//import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by Adhamkh on 2018-07-03.
 */
class City {
    var nameAr: String = ""
    var nameEn: String = ""
    var id: String = ""

    var locations: MutableList<LocationEntity> = mutableListOf()

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
        if (other !is City) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}