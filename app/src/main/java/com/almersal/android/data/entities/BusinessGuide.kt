package com.almersal.android.data.entities

import com.almersal.android.App

class BusinessGuide {

    var vip = false
    var nameEn: String = ""
    var nameAr: String = ""

    var logo: String = ""
    var status: String = ""
    var description: String = ""
    var openingDays: MutableList<String> = mutableListOf()
    var openingDaysEnabled: Boolean = false
    var id: String = ""
    var ownerId: String = ""
    var products: MutableList<Product> = mutableListOf()
    var myMarketProducts: MutableList<Product> = mutableListOf()

    var categoryId: String = ""
    var subCategoryId: String = ""
    var cityId: String = ""
    var locationId: String = ""
    var covers: MutableList<MediaEntity> = mutableListOf()
    var cover: String = ""

    var owner: Owner = Owner()

    var locationPoint: PointEntity = PointEntity()

    var phone1: String = ""

    var phone2: String = ""
    var fax: String = ""

    var category: BusinessGuideCategory = BusinessGuideCategory()
    var subCategory: SubCategory = SubCategory()

    var city: City = City()
    var location: LocationEntity = LocationEntity()
    var isSelected = false


    constructor()

    constructor(logo: String, nameAr: String, nameEn: String, category: BusinessGuideCategory, subCategory: SubCategory) {
        this.logo = logo
        this.nameAr = nameAr
        this.nameEn = nameEn
        this.category = category
        this.subCategory = subCategory
    }

    fun getName(): String {

        return if (App.app.isArabic()) nameAr else nameAr
    }


}