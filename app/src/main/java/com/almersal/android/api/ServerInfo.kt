package com.almersal.android.api


class ServerInfo {

    companion object {
        //        var baseUrl = "http://almersal.co/api/"
        var baseUrl = "http://157.230.121.161:3000/api/"
//        var baseUrl = "http://192.168.1.11:3000/api/"

        /*Registration started*/
        var SigninUrl = baseUrl + "users/login/?include=user"

        var SignUpUrl = baseUrl + "users/"

        var ForgotPasswordUrl = baseUrl + "users/forgotPassword/"

        /*Registration ended*/

        /*Profile started*/
        var updateProfile = baseUrl + "users"//"users/update"
        var getUserResume = updateProfile + "/"

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

        var uploadVideoUrl = baseUrl + "attachments/videos/upload"


        /*Notification started*/
        var notificationUrl = baseUrl + "notifications"
        var notificationSeenUrl = notificationUrl + "/seenNotification"
        var notificationClickedUrl = notificationUrl
        var notificationClear = notificationUrl + "/clear"
        var fireBaseNotificationUrl = baseUrl + "users/fcmToken"
        var tagsUrl = baseUrl + "tags"
        var cvUrl = baseUrl + "userCVs/updateMyCv"


        var jobsUrl = baseUrl + "jobOpportunities/"
        var jobDetailsUrl = "/getJobOpportunity"
        var updateJobDetailsUrl = "/updateJobOpportunity"
        var addNewJob = "/addJobOpportunity"
        var jobApplicants = "/employee"



        var jobSearchUrl = "${jobsUrl}searchJob"
        var jobCategoriesUrl = baseUrl + "jobOpportunityCategories"
        var jobUserUrl = baseUrl + "jobOpportunityUsers/"

        /*Notification ended*/

    }

}