package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-06-29.
 */
class SubCategory : Category {
    constructor()

    constructor(titleAr: String, titleEn: String, id: String) : super(titleAr, titleEn, id) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }

    var parentCategoryId: String = ""
}