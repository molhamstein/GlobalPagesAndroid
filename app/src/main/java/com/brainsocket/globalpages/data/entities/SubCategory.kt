package com.brainsocket.globalpages.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by Adhamkh on 2018-06-29.
 */
@JsonIgnoreProperties("subCategories")
class SubCategory : Category {
    constructor()

    constructor(titleAr: String, titleEn: String, id: String) : super(titleAr, titleEn, id) {
        this.titleAr = titleAr
        this.titleEn = titleEn
        this.id = id
    }

    var parentCategoryId: String = ""

    override var subCategoriesList: MutableList<SubCategory>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}




}