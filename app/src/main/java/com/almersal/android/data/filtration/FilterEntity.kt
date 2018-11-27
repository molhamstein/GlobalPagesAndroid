package com.almersal.android.data.filtration

import com.almersal.android.data.entities.*
import com.almersal.android.enums.TagType

/**
 * Created by Adhamkh on 2018-08-15.
 */
class FilterEntity {

    var query: String? = null

    var postCategory: PostCategory? = null

    var subCategory: SubCategory? = null

    var city: City? = null

    var area: LocationEntity? = null

    constructor()

    constructor(query: String? = null, postCategory: PostCategory? = null,
                subCategory: SubCategory? = null,
                city: City? = null,
                area: LocationEntity? = null) {
        this.query = query
        this.postCategory = postCategory
        this.subCategory = subCategory
        this.city = city
        this.area = area
    }

    fun getTags(): MutableList<TagEntity> {
        val tagEntityList = mutableListOf<TagEntity>()

        if (postCategory != null) {
            val tag = TagEntity(titleAr = postCategory!!.titleAr, titleEn = postCategory!!.titleEn,
                    tagId = postCategory!!.id, tagType = TagType.Category)
            tagEntityList.add(tag)
        }
        if (subCategory != null) {
            val tag = TagEntity(titleAr = subCategory!!.titleAr, titleEn = subCategory!!.titleEn,
                    tagId = subCategory!!.id, tagType = TagType.SubCategory)
            tagEntityList.add(tag)
        }
        if (city != null) {
            val tag = TagEntity(titleAr = city!!.nameAr, titleEn = city!!.nameEn,
                    tagId = city!!.id, tagType = TagType.City)
            tagEntityList.add(tag)
        }
        if (area != null) {
            val tag = TagEntity(titleAr = area!!.nameAr, titleEn = area!!.nameEn,
                    tagId = area!!.id, tagType = TagType.Location)
            tagEntityList.add(tag)
        }

        return tagEntityList
    }

    fun getCriteriaFromTags(tagEntityList: MutableList<TagEntity>) {

    }


}