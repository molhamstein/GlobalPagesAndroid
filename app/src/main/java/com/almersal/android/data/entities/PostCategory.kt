package com.almersal.android.data.entities

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties

//@JsonIgnoreProperties("parentCategoryId")
class PostCategory : Category {

    constructor()

    constructor(titleAr: String, titleEn: String, id: String) : super(titleAr, titleEn, id)

    var subCategories: MutableList<SubCategory> = mutableListOf()

    override var subCategoriesList: MutableList<SubCategory>
        get() = subCategories
        set(value) {
            subCategories = value
        }




}

