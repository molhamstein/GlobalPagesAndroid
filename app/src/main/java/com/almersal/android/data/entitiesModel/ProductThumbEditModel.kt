package com.almersal.android.data.entitiesModel

class ProductThumbEditModel : ProductThumbModel {
    var id: String? = ""

    constructor() : super()

    constructor(name: String, price: String, image: String, description: String, id: String?) :
            super(name, price, image, description) {
        this.id = id
    }


}