package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-07-05.
 */
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