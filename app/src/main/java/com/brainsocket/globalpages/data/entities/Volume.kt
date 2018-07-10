package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-06-29.
 */
class Volume {
    var titleAr: String = ""
    var titleEn: String = ""
    var status: String = ""
    var creationDate: String = ""

    var id: String = ""

    var postsIds: MutableList<String> = mutableListOf()

    var posts: MutableList<Post> = mutableListOf()

}