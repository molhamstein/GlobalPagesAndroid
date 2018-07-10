package com.brainsocket.globalpages.api

/**
 * Created by Adhamkh on 2018-03-25.
 */
class ServerInfo {

    companion object {
        var baseUrl = "http://104.217.253.15:3000/api/"

        var SigninUrl = baseUrl + "users/login/?include=user"

        var SignupUrl = baseUrl + "users/"

        var ForgotPasswordUrl = baseUrl + "users/forgotPassword/"

        var VolumeUrl = baseUrl + "volumes"

//        var imageUrl = "https://cdn.mangaeden.com/mangasimg/"
    }

}