package com.almersal.android.data.entities

import com.almersal.android.App
import com.almersal.android.enums.TagType

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