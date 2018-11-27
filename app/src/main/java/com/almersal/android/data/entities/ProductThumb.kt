package com.almersal.android.data.entities

class ProductThumb {

    var id: String = ""
    var name: String = ""
    var price: String = "10"
    var image: String = ""
    var description: String = ""

    constructor(id: String, name: String, price: String, image: String, description: String) {
        this.id = id
        this.name = name
        this.price = price
        this.image = image
        this.description = description
    }
}