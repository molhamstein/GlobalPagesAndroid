package com.almersal.android.data.entitiesModel

import com.almersal.android.data.entities.MediaEntity

/**
 * Created by Adhamkh on 2018-10-09.
 */
open class PostModel {

    var title: String = ""
    //    var name: String = ""
    var description: String = ""
    var status: String = "pending"
    var viewsCount: String = ""
    var isFeatured: Boolean = false
    var ownerId: String = ""
    var categoryId: String = ""
    var subCategoryId: String = ""
    var cityId: String = ""
    var locationId: String = ""
    var media: MutableList<MediaEntity> = mutableListOf()
}