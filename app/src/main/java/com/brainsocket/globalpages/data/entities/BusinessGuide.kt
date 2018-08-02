package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App

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
        return if (App.app.isArabic()) nameAr else nameAr
    }


}