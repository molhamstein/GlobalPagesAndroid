package com.brainsocket.globalpages.data.entities

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//@JsonIgnoreProperties("subCategories")
class SubCategory : Category {
    constructor()

    constructor(titleAr: String, titleEn: String, id: String) : super(titleAr, titleEn, id) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }

    var parentCategoryId: String = ""

    override var subCategoriesList: MutableList<SubCategory>
        get() = mutableListOf()
        set(value) {}




}