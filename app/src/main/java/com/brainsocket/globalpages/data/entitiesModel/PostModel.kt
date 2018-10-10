package com.brainsocket.globalpages.data.entitiesModel

import com.brainsocket.globalpages.data.entities.MediaEntity

/**
 * Created by Adhamkh on 2018-10-09.
 */
class PostModel {

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