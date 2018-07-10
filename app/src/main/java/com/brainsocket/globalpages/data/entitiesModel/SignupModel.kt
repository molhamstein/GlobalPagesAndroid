package com.brainsocket.globalpages.data.entitiesModel

import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.normalization.DateNormalizer
import java.util.*

/**
 * Created by Adhamkh on 2018-06-17.
 */
class SignupModel {
    var email: String = ""
    var username: String? = null
    var password: String = ""
    var gender: String? = UserGender.male.gender
    var birthdate: String? = DateNormalizer.getCanonicalDateTimeFormat(Calendar.getInstance().time)

    var termAndCondition: Boolean = false
}