package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-07-18.
 */
class BusinessGuide {

    var imageUrl: String = ""
    var nameAr: String = ""
    var nameEn: String = ""

    var category: Category = Category()
    var subCategory: SubCategory = SubCategory()

    constructor(imageUrl: String, nameAr: String, nameEn: String, category: Category, subCategory: SubCategory) {
        this.imageUrl = imageUrl
        this.nameAr = nameAr
        this.nameEn = nameEn
        this.category = category
        this.subCategory = subCategory
    }

    fun getName(): String {
        return nameAr
    }


}