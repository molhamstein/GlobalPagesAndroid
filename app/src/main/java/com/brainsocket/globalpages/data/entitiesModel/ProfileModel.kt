package com.brainsocket.globalpages.data.entitiesModel

import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.enums.UserStatus
import com.brainsocket.globalpages.normalization.DateNormalizer
import java.util.*

class ProfileModel {
    var email: String = ""
    var username: String? = null

    var gender: String? = UserGender.male.gender
    var status: String = UserStatus.pending.status
    var birthDate: String? = DateNormalizer.getCanonicalDateTimeFormat(Calendar.getInstance().time)

    var imageProfile: String = ""
    var phoneNumber: String = ""

    var creationDate: String? = null
    var emailVerified: Boolean? = true
    var id: String? = null
    var termAndCondition: Boolean? = true

    var postCategoriesIds: MutableList<String>? = null

    constructor()
    constructor(email: String, username: String?, gender: String?, status: String, birthDate: String?, imageProfile: String, phoneNumber: String, creationDate: String?, emailVerified: Boolean?, id: String?, termAndCondition: Boolean?, postCategoriesIds: MutableList<String>?) {
        this.email = email
        this.username = username
        this.gender = gender
        this.status = status
        this.birthDate = birthDate
        this.imageProfile = imageProfile
        this.phoneNumber = phoneNumber
        this.creationDate = creationDate
        this.emailVerified = emailVerified
        this.id = id
        this.termAndCondition = termAndCondition
        this.postCategoriesIds = postCategoriesIds
    }


}