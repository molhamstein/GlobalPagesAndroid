package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-07-04.
 */
class TagEntity {

    constructor()

    constructor(titleAr: String, titleEn: String) {
        this.titleAr = titleAr
        this.titleEn = titleEn
    }

    var titleAr: String = ""
    var titleEn: String = ""
    var tagId: String=""
    var isSelected = false

    fun getTitle(): String {
        return titleAr
    }
}