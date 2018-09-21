package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App
//import com.fasterxml.jackson.annotation.JsonIgnore
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//@JsonIgnoreProperties("subCategories")
abstract class Category {

    var titleAr: String = ""
    var titleEn: String = ""
    var creationDate: String = ""
    var id: String = ""

    abstract var subCategoriesList: MutableList<SubCategory>

//    @JsonIgnore
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}