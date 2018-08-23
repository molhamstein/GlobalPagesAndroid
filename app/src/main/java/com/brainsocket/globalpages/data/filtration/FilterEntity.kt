package com.brainsocket.globalpages.data.filtration

import com.brainsocket.globalpages.data.entities.*

/**
 * Created by Adhamkh on 2018-08-15.
 */
class FilterEntity {

    var query: String? = null

    var posCategory: PostCategory? = null

    var subCategory: SubCategory? = null

    var city: City? = null

    var area: LocationEntity? = null

    constructor()

    constructor(query: String? = null, posCategory: PostCategory? = null,
                subCategory: SubCategory? = null,
                city: City? = null,
                area: LocationEntity? = null) {
        this.query = query
        this.posCategory = posCategory
        this.subCategory = subCategory
        this.city = city
        this.area = area
    }


}