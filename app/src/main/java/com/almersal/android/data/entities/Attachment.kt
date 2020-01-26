package com.almersal.android.data.entities


class Attachment {
    var name: String = ""
    var thumbnail: String = ""
    var type: String = ""

    var size: String = ""
    var atime: String = ""
    var mtime: String = ""
    var ctime: String = ""

    constructor()

//    constructor(name: String, type: String = "images",thumbnail: String) {
//        this.name = name
//        this.type = type
//        this.thumbnail = ""
//    }


    constructor(name: String, type: String = "image", thumbnail: String  = name) {
        this.name = name
        this.type = type
        this.thumbnail = thumbnail
    }
}