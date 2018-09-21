package com.brainsocket.globalpages.data.entities

import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.enums.UserStatus
import com.brainsocket.globalpages.normalization.DateNormalizer
import java.util.*

class User {
    var email: String = ""
    var username: String? = null

    var gender: String? = UserGender.male.gender
    var status: String = UserStatus.pending.status
    var birthdate: String? = DateNormalizer.getCanonicalDateTimeFormat(Calendar.getInstance().time)

    var creationDate: String? = null
    var emailVerified: Boolean? = true
    var id: String? = null
    var termAndCondition: Boolean? = true

    var imageProfile: String = ""
    var phoneNumber: String = ""

    var postCategoriesIds: MutableList<String>? = null

    class ObjectID {
        var Id: String = ""
    }

}