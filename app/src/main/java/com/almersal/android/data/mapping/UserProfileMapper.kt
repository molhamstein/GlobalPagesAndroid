package com.almersal.android.data.mapping

import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesModel.ProfileModel

/**
 * Created by Adhamkh on 2018-09-21.
 */
class UserProfileMapper {

    companion object {

        fun userProfileTransform(user: User): ProfileModel {
            return ProfileModel(email = user.email, username = user.username, gender = user.gender, status = user.status,
                    birthDate = user.birthdate, imageProfile = user.imageProfile, phoneNumber = user.phoneNumber,
                    creationDate = user.creationDate, emailVerified = user.emailVerified, id = user.id, termAndCondition = user.termAndCondition,
                    postCategoriesIds = user.postCategoriesIds)
        }

    }

}