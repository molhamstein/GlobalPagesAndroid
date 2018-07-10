package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-07-03.
 */
class LocationEntity {
    var titleAr: String = ""
    var titleEn: String = ""
    var id: String = ""

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