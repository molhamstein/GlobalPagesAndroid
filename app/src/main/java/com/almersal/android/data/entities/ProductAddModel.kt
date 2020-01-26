package com.almersal.android.data.entities

import com.almersal.android.App

class ProductAddModel {

    var id: String? = null
    var titleAr: String = ""
    var titleEn: String = ""
    var descriptionAr: String = ""
    var descriptionEn: String = ""
    var status: String = "pending"
    var ownerId: String = ""
    var categoryId: String = ""
    var subCategoryId: String = ""
    var cityId: String = ""
    var locationId: String = ""
    var price = 0
    var businessId: String? = null
    var media: MutableList<String> = mutableListOf()
    var title: String? = null
        get() {
            if (!field.isNullOrEmpty()) return field
            return if (App.app.isArabic()) titleAr else titleEn
        }
}