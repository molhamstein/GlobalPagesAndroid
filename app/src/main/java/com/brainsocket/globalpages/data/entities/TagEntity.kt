package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App
import com.brainsocket.globalpages.enums.TagType

/**
 * Created by Adhamkh on 2018-07-04.
 */
class TagEntity {

    var titleAr: String = ""
    var titleEn: String = ""
    var tagId: String = ""
    var tagType = TagType.Category
    var isSelected = false

    constructor()

    constructor(titleAr: String, titleEn: String) {
        this.titleAr = titleAr
        this.titleEn = titleEn
    }

    constructor(titleAr: String, titleEn: String, tagId: String, tagType: TagType) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.tagId = tagId
        this.tagType = tagType
    }


    fun getTitle(): String {
        return if (App.app.isArabic()) titleAr else titleEn
    }
}