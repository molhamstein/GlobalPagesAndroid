package com.brainsocket.globalpages.data.entities

/**
 * Created by Adhamkh on 2018-07-01.
 */
class Owner {

    var status: String = ""
    var birthDate: String = ""
    var phoneNumber: String = ""
    var gender: String = ""
    var creationDate: String = ""
    var username: String = ""
    var email: String = ""
    var emailVerified: Boolean = true
    var id: String = ""
    var postCategoriesIds: MutableList<ObjectID>? = null

    var image = ""

    class ObjectID {
        var Id: String = ""
    }
}