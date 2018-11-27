package com.almersal.android.data.entities

class MediaEntity {
    var id: String = ""
    var url: String = ""
    var type: String = ""
    var thumbnail: String = ""

    constructor()

    constructor(url: String) {
        this.url = url
    }


}