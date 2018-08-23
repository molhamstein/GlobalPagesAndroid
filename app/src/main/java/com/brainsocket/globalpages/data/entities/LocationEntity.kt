package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by Adhamkh on 2018-07-03.
 */
class LocationEntity {
    var nameAr: String = ""
    var nameEn: String = ""
    var id: String = ""
    var cityId: String = ""

    @JsonIgnore
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
}