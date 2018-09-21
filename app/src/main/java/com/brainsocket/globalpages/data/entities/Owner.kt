package com.brainsocket.globalpages.data.entities

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
    var postCategoriesIds: MutableList<String>? = null

    var image = ""

    var imageProfile = ""

    class ObjectID {
        var Id: String = ""
    }
}