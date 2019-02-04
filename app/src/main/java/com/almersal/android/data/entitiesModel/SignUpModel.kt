package com.almersal.android.data.entitiesModel

import com.almersal.android.enums.UserGender
import com.almersal.android.normalization.DateNormalizer
import java.util.*


class SignUpModel {
    var email: String = ""
    var username: String? = null
    var password: String = ""
    var gender: String? = UserGender.male.gender
    var birthdate: String? = DateNormalizer.getCanonicalDateTimeFormat(Calendar.getInstance().time)
    var phoneNumber: String = ""

    var termAndCondition: Boolean = false
}