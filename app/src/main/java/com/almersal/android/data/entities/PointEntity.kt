package com.almersal.android.data.entities

/**
 * Created by Adhamkh on 2018-09-17.
 */
class PointEntity {
    var lat: Double = 0.0
    var lng: Double = 0.0

    constructor()
    constructor(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
    }


}