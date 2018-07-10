package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-06-29.
 */
open class Category {

    var titleAr: String = ""
    var titleEn: String = ""
    var creationDate: String = ""
    var id: String = ""

    constructor()

    constructor(titleAr: String, titleEn: String, id: String) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }

    fun getTitle(): String {
        return titleEn
    }

}