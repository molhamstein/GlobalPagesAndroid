package com.brainsocket.globalpages.api

/**
 * Created by Adhamkh on 2018-03-25.
 */
class ServerInfo {

    companion object {
        var baseUrl = "http://104.217.253.15:3000/api/"

        /*Registration started*/
        var SigninUrl = baseUrl + "users/login/?include=user"

        var SignUpUrl = baseUrl + "users/"

        var ForgotPasswordUrl = baseUrl + "users/forgotPassword/"

        /*Registration ended*/



        /*Tagging started*/
        var businessCategoriesUrl = baseUrl + "businessCategories"

        var postCategoriesUrl = baseUrl + "postCategories"

        var citiesUrl = baseUrl + "cities"
        /*Tagging ended*/


        var volumeUrl = baseUrl + "volumes"

        var businessGuideUrl = baseUrl + "businesses"


        var imagesBaseUrl = baseUrl + "attachments/images/upload/"

    }

}