package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-06-29.
 */
class Post {

    constructor() {

    }

    constructor(title: String, description: String, status: String, image: String, category: Category, subCategory: SubCategory) {
        this.title = title
        this.description = description
        this.status = status
        this.image = image
        this.category = category
        this.subCategory = subCategory
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

    var category: Category = Category()

    var subCategory: SubCategory = SubCategory()

}