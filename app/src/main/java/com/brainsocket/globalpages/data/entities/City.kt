package com.brainsocket.globalpages.data.entities

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by Adhamkh on 2018-07-03.
 */
class City {
    var titleAr: String = ""
    var titleEn: String = ""
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
        return titleAr
    }

}