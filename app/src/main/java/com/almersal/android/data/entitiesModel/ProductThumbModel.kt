package com.almersal.android.data.entitiesModel

/**
 * Created by Adhamkh on 2018-10-07.
 */
open class ProductThumbModel {

    var name: String = ""
    var price: String = "10"
    var image: String = ""
    var description: String = ""

    constructor()

    constructor(name: String, price: String, image: String, description: String) {
        this.name = name
        this.price = price
        this.image = image
        this.description = description
    }


}