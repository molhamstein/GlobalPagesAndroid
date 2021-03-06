package com.almersal.android.data.entitiesModel

import com.almersal.android.data.entities.MediaEntity
import com.almersal.android.data.entities.PointEntity


open class BusinessGuideModel {


    var nameEn: String = ""
    var nameAr: String = ""

    var description: String = ""

    var locationPoint: PointEntity = PointEntity()
    var phone1: String = ""
    var phone2: String = ""
    var fax: String = ""
    //    var id: String = ""
//    var userId: String = ""
    var ownerId: String = ""
    var categoryId: String = ""
    var subCategoryId: String = ""
    var cityId: String = ""
    var locationId: String = ""

    var covers: MutableList<MediaEntity> = mutableListOf()

    var openingDays: MutableList<String> = mutableListOf()
    var openingDaysEnabled: Boolean = false

//    var trim: String = ""

}