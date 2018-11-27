package com.almersal.android.data.entities

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//@JsonIgnoreProperties("subCategories")
class SubCategory : Category {
    constructor()

    constructor(titleAr: String, titleEn: String, id: String) : super(titleAr, titleEn, id) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }



    override var subCategoriesList: MutableList<SubCategory>
        get() = mutableListOf()
        set(value) {}




}