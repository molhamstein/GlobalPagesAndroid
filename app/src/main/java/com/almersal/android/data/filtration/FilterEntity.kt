package com.almersal.android.data.filtration

import com.almersal.android.data.entities.*
import com.almersal.android.enums.TagType
import com.almersal.android.repositories.DummyDataRepositories

class FilterEntity {

    var query: String? = null

    var category: Category? = null

    var subCategory: SubCategory? = null

    var city: City? = null

    var area: LocationEntity? = null

    constructor()

    constructor(query: String? = null, category: Category? = null,
                subCategory: SubCategory? = null,
                city: City? = null,
                area: LocationEntity? = null) {
        this.query = query
        this.category = category
        this.subCategory = subCategory
        this.city = city
        this.area = area
    }

    fun getTags(): MutableList<TagEntity> {
        val tagEntityList = mutableListOf<TagEntity>()

        if (category != null) {
            val tag = TagEntity(titleAr = category!!.titleAr, titleEn = category!!.titleEn,
                    tagId = category!!.id, tagType = TagType.Category)
            tagEntityList.add(tag)
        }
        if (subCategory != null) {
            val tag = TagEntity(titleAr = subCategory!!.titleAr, titleEn = subCategory!!.titleEn,
                    tagId = subCategory!!.id, tagType = TagType.SubCategory)
            tagEntityList.add(tag)
        }
        if ((category == null) && (subCategory == null)) {
            tagEntityList.add(DummyDataRepositories.getTagsDefaultRepositories()[0])
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
        if ((city == null) && (area == null)) {
            tagEntityList.add(DummyDataRepositories.getTagsDefaultRepositories()[1])
        }

        if (!query.isNullOrBlank()) {
            val tag = TagEntity(titleAr = query!!, titleEn = query!!,
                    tagId = "", tagType = TagType.Query)
            tagEntityList.add(tag)
        }

        return tagEntityList
    }

    fun getCriteriaFromTags(tagEntityList: MutableList<TagEntity>?): FilterEntity {
        val filterEntity = FilterEntity()
        tagEntityList?.forEach {
            when (it.tagType) {
                TagType.Query -> {
                    filterEntity.query = it.titleEn
                }
                TagType.Category -> {
                    if (!it.tagId.isBlank()) {
                        filterEntity.category = PostCategory(titleEn = it.titleEn, titleAr = it.titleAr, id = it.tagId)
                    }
                }
                TagType.SubCategory->{
                    if (!it.tagId.isBlank()) {
                        filterEntity.subCategory = SubCategory(titleEn = it.titleEn, titleAr = it.titleAr, id = it.tagId)
                    }
                }
                TagType.City->{
                    if (!it.tagId.isBlank()) {
                        filterEntity.city = City(titleEn = it.titleEn, titleAr = it.titleAr, id = it.tagId)
                    }
                }
                TagType.Location->{
                    if (!it.tagId.isBlank()) {
                        filterEntity.area = LocationEntity(titleEn = it.titleEn, titleAr = it.titleAr, id = it.tagId)
                    }
                }
            }
        }
        return filterEntity
    }



}