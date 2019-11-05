package com.almersal.android.data.entities

import com.almersal.android.enums.UserGender
import com.almersal.android.enums.UserStatus
import com.almersal.android.normalization.DateNormalizer
import java.util.*

class User {

    /**id from Login response**/
    var token: String = ""

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
    var CV: UserResume? = null

    class ObjectID {
        var Id: String = ""
    }

}