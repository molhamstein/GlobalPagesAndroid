package com.almersal.android.api


class ServerInfo {

    companion object {
        var baseUrl = "http://almersal.co/api/"

        /*Registration started*/
        var SigninUrl = baseUrl + "users/login/?include=user"

        var SignUpUrl = baseUrl + "users/"

        var ForgotPasswordUrl = baseUrl + "users/forgotPassword/"

        /*Registration ended*/

        /*Profile started*/
        var updateProfile = baseUrl + "users"//"users/update"

        /*Profile ended*/


        /*Tagging started*/
        var businessCategoriesUrl = baseUrl + "businessCategories"

        var postCategoriesUrl = baseUrl + "postCategories"

        var citiesUrl = baseUrl + "cities"
        /*Tagging ended*/


        var volumeUrl = baseUrl + "volumes"

        var businessGuideUrl = baseUrl + "businesses"


        var imagesBaseUrl = baseUrl + "attachments/images/upload/"

        var postUrl = baseUrl + "posts"

        var uploadImageUrl = baseUrl + "attachments/images/upload"


        /*Notification started*/
        var notificationUrl = baseUrl + "notifications"

        var fireBaseNotificationUrl = baseUrl + "users/fcmToken"

        /*Notification ended*/

    }

}