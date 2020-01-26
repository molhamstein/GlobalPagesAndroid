package com.almersal.android.data.entities

import com.almersal.android.App
import com.almersal.android.enums.PostType
//import com.fasterxml.jackson.annotation.JsonProperty
//import com.fasterxml.jackson.databind.annotation.JsonNaming
//import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.google.gson.annotations.SerializedName

open class Product {

    constructor()

    constructor(
        title: String, description: String, status: String, image: String,
        category: PostCategory, subCategory: SubCategory, city: City, area: LocationEntity
    ) {
        this.title = title
        this.description = description
        this.status = status
        this.image = image
        this.category = category
        this.subCategory = subCategory
        this.city = city
        this.location = area
    }

    var businessId = ""
    var title: String? = null
        get() {
            if (!field.isNullOrEmpty()) return field
            return if (App.app.isArabic()) titleAr else titleEn
        }
    var titleAr: String? = null
    var titleEn: String? = null
    var description: String = ""
        get() {
            if (field.isNotEmpty()) return field
            return if (App.app.isArabic()) descriptionAr else descriptionEn

        }

    var descriptionAr: String = ""
    var descriptionEn: String = ""
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
    var price:Int? = null


    //    @JsonProperty("isFeatured")
    @SerializedName("isFeatured")
    var featured: String = ""

    val media: MutableList<String> = mutableListOf()
    fun getMedias(): MutableList<MediaEntity> {
        val result = mutableListOf<MediaEntity>()
        media.forEach {
            result.add(MediaEntity(it, "image", it))
        }
        return result
    }


    var owner: Owner = Owner()

    var category: PostCategory = PostCategory()

    var subCategory: SubCategory = SubCategory()

    var city: City = City()
    var location: LocationEntity = LocationEntity()

    val business: BusinessGuide? = null
    fun getPostType(): Int {
        return PostType.ONLY_IMAGE.type
    }

    fun getRealCreationDate() {

    }
//
//
//    fun getDescription(): String {
//
//    }


}