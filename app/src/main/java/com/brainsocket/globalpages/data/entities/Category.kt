package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by Adhamkh on 2018-06-29.
 */
open class Category {

    var titleAr: String = ""
    var titleEn: String = ""
    var creationDate: String = ""
    var id: String = ""

    @JsonIgnore
    var isSelected: Boolean = false


    constructor()

    constructor(titleAr: String, titleEn: String, id: String) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }

    fun getTitle(): String {
        return if (App.app.isArabic()) titleAr else titleEn
    }

}