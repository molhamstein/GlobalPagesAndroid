package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.enums.PostType

/**
 * Created by Adhamkh on 2018-06-29.
 */
class Post {

    constructor() {

    }

    constructor(title: String, description: String, status: String, image: String,
                category: PostCategory, subCategory: SubCategory, city: City, area: LocationEntity) {
        this.title = title
        this.description = description
        this.status = status
        this.image = image
        this.category = category
        this.subCategory = subCategory
        this.city = city
        this.area = area
    }

    var title: String = ""
    var description: String = ""
    var status: String = ""
    var image: String = ""
    var viewsCount: String = ""
    var creationDate: String = ""
    var id: String = ""
    var ownerId: String = ""
    var categoryId: String = ""
    var subCategoryId: String = ""

    var media: MutableList<Any> = mutableListOf()

    var owner: Owner = Owner()

    var category: PostCategory = PostCategory()

    var subCategory: SubCategory = SubCategory()

    var city: City = City()
    var area: LocationEntity = LocationEntity()

    fun getPostType(): Int {
        if (!image.isNullOrEmpty()) {
            return PostType.WITH_IMAGE.type
        }
        return PostType.WITH_TEXT.type
    }


}