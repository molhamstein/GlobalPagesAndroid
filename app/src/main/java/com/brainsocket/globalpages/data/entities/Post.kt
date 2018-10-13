package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.enums.PostType
//import com.fasterxml.jackson.annotation.JsonProperty
//import com.fasterxml.jackson.databind.annotation.JsonNaming
//import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.google.gson.annotations.SerializedName

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
        this.location = area
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

    var cityId: String = ""
    var locationId: String = ""

    var userId: String = ""


    //    @JsonProperty("isFeatured")
    @SerializedName("isFeatured")
    var featured: String = ""

    var media: MutableList<MediaEntity> = mutableListOf()

    var owner: Owner = Owner()

    var category: PostCategory = PostCategory()

    var subCategory: SubCategory = SubCategory()

    var city: City = City()
    var location: LocationEntity = LocationEntity()

    fun getPostType(): Int {
        if (media.size > 0) {
            return PostType.WITH_IMAGE.type
        }
        return PostType.WITH_TEXT.type
    }

    fun getRealCreationDate() {

    }


}