package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.App

class Volume {
    var titleAr: String = ""
    var titleEn: String = ""
    var status: String = ""
    var creationDate: String = ""

    var id: String = ""

    var postsIds: MutableList<String> = mutableListOf()

    var posts: MutableList<Post> = mutableListOf()

    fun getTitle(): String {
        return if (App.app.isArabic()) titleAr else titleEn
    }

}