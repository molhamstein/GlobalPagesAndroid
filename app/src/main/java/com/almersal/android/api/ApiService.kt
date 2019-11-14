package com.almersal.android.api

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesResponses.LoginResponse
import com.google.gson.Gson
import java.io.File
import com.androidnetworking.interfaces.*
import com.almersal.android.data.entitiesModel.*
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.almersal.android.data.entitiesResponses.NotificationReadedResponse
import com.almersal.android.repositories.UserRepository
import java.util.HashMap


class ApiService {


    /*Registration started*/
    fun postUserLogin(url: String, loginModel: LoginModel, requestListener: ParsedRequestListener<LoginResponse>) {
        AndroidNetworking.post(url)
            .addBodyParameter(loginModel)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(LoginResponse::class.java, requestListener)
    }

    fun postUserForgotPassword(
        url: String,
        forgotPasswordModel: ForgotPasswordModel,
        requestListener: JSONObjectRequestListener
    ) {
        val jSon = Gson().toJson(forgotPasswordModel)
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
//                .addApplicationJsonBody(forgotPasswordModel.email)
            .build()
            .getAsJSONObject(requestListener)
    }

    fun postUserSignUp(url: String, signUpModel: SignUpModel, requestListener: ParsedRequestListener<User>) {
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .setPriority(Priority.HIGH)
            .addBodyParameter(signUpModel)
            .build()
            .getAsObject(User::class.java, requestListener)
    }
    /*Registration ended*/


    /*User Profile started*/
    fun getUserPosts(
        url: String/*, filtration: MutableMap<String, Pair<String, String>>*/,
        requestListener: ParsedRequestListener<MutableList<Post>>
    ) {
//        val criteria = Gson().toJson(filtration)

        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(Post::class.java, requestListener)
    }

    fun getUserBusinesses(
        url: String, /*filtration: MutableMap<String, Pair<String, String>>,*/
        requestListener: ParsedRequestListener<MutableList<BusinessGuide>>
    ) {
//        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(BusinessGuide::class.java, requestListener)
    }


    //Old depricated with the call UpdateProfile
    fun updateUserProfile(
        url: String,
        profile: ProfileModel,
        token: String,
        requestListener: ParsedRequestListener<User>
    ) {
        val filtration: MutableMap<String, Pair<String, String>> = HashMap()//[ownerId]
//        filtration["where"] = Pair("id", profile.id!!)

        val jSon = Gson().toJson(profile)
        AndroidNetworking.put(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
//                .addQueryParameter("filter", criteria)
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
            .build()
            .getAsObject(User::class.java, requestListener)
    }
    /*User Profile ended*/


    /*Tagging started*/
    fun getBusinessCategories(
        url: String,
        filtration: Map<String, String>,
        requestListener: ParsedRequestListener<MutableList<BusinessGuideCategory>>
    ) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//              .setContentType("application/json")
            .addQueryParameter("filter", criteria)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(BusinessGuideCategory::class.java, requestListener)
    }

    fun getPostCategories(
        url: String,
        filtration: Map<String, String>,
        requestListener: ParsedRequestListener<MutableList<PostCategory>>
    ) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .addQueryParameter("filter", criteria)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(PostCategory::class.java, requestListener)
    }

    fun getCities(
        url: String,
        filtration: Map<String, String>,
        requestListener: ParsedRequestListener<MutableList<City>>
    ) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .addQueryParameter("filter", criteria)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(City::class.java, requestListener)
    }
    /*Tagging ended*/


    /*Volumes started*/
    fun getVolumes(
        url: String/*, filtration: Map<String, String>*/,
        requestListener: ParsedRequestListener<MutableList<Volume>>
    ) {
//        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(Volume::class.java, requestListener)
    }
    /*Volumes ended*/


    /*JobBusiness Guides started*/
    fun getBusinessGuides(url: String, requestListener: ParsedRequestListener<MutableList<BusinessGuide>>) {
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(BusinessGuide::class.java, requestListener)
    }

    fun getBusinessGuide(url: String, requestListener: ParsedRequestListener<BusinessGuide>) {
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObject(BusinessGuide::class.java, requestListener)
    }

    fun postBusinessGuides(
        url: String,
        businessGuide: BusinessGuideModel,
        token: String,
        requestListener: ParsedRequestListener<BusinessGuide>
    ) {
        val json = Gson().toJson(businessGuide)
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(json)
//                .setPriority(Priority.HIGH)
//                .addPathParameter("userId", businessGuide.ownerId)
//                .addQueryParameter("userId", businessGuide.ownerId)
            .addBodyParameter(businessGuide)
            .build()
            .getAsObject(BusinessGuide::class.java, requestListener)
    }

    fun putBusinessGuides(
        url: String,
        businessGuide: BusinessGuideEditModel,
        token: String,
        requestListener: ParsedRequestListener<BusinessGuide>
    ) {
        val json = Gson().toJson(businessGuide)
        AndroidNetworking.put(url + "/" + businessGuide.id)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(json)
//                .setPriority(Priority.HIGH)
//                .addPathParameter("userId", businessGuide.ownerId)
//                .addQueryParameter("userId", businessGuide.ownerId)
            .addBodyParameter(businessGuide)
            .build()
            .getAsObject(BusinessGuide::class.java, requestListener)
    }

    /*JobBusiness Guides ended*/

    /*Post started*/

    fun getPost(url: String, requestListener: ParsedRequestListener<Post>) {
//        val criteria = Gson().toJson(filtration)

        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObject(Post::class.java, requestListener)
    }

    fun postPost(url: String, postModel: PostModel, token: String, requestListener: ParsedRequestListener<Post>) {
        val json = Gson().toJson(postModel)
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(json)
//                .setPriority(Priority.HIGH)
//                .addPathParameter("userId", businessGuide.ownerId)
//                .addQueryParameter("userId", businessGuide.ownerId)
            .addBodyParameter(postModel)
            .build()
            .getAsObject(Post::class.java, requestListener)
    }

    fun putPost(url: String, postModel: PostEditModel, token: String, requestListener: ParsedRequestListener<Post>) {
        val json = Gson().toJson(postModel)
        AndroidNetworking.put(url + "/" + postModel.id)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(json)
//                .setPriority(Priority.HIGH)
//                .addPathParameter("userId", businessGuide.ownerId)
//                .addQueryParameter("userId", businessGuide.ownerId)
            .addBodyParameter(postModel)
            .build()
            .getAsObject(Post::class.java, requestListener)
    }

    fun getFeaturedPosts(url: String, requestListener: ParsedRequestListener<MutableList<Post>>) {

        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(Post::class.java, requestListener)
    }

    /*Post ended*/


    /*Attachment started*/

    fun uploadAttachment(
        url: String,
        file: File,
        uploadProgressListener: UploadProgressListener,
        requestListener: ParsedRequestListener<MutableList<AttachmentResponse>>
    ) {
        if (file.exists()) {
            Log.v("Uploaded File", "Existed")
        }
        AndroidNetworking.upload(url)
            .addMultipartFile("file", file)
            .addMultipartParameter("key", "value")
            .setTag("uploadAttachment")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener(uploadProgressListener)
            .getAsObjectList(AttachmentResponse::class.java, requestListener)


    }

    /*Attachment ended*/


    /*JobBusiness Product started*/
    fun postBusinessGuideProduct(
        url: String,
        productThumbModel: ProductThumbModel,
        token: String,
        requestListener: ParsedRequestListener<ProductThumb>
    ) {
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addBodyParameter(productThumbModel)
            .build()
            .getAsObject(ProductThumb::class.java, requestListener)
    }

    fun putBusinessGuideProduct(
        url: String,
        productThumbModel: ProductThumbEditModel,
        token: String,
        requestListener: ParsedRequestListener<ProductThumb>
    ) {
        AndroidNetworking.put(url + "/" + productThumbModel.id)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addBodyParameter(productThumbModel)
            .build()
            .getAsObject(ProductThumb::class.java, requestListener)
    }

    /*JobBusiness Product ended*/


    /*Notification started*/
    fun getNotifications(url: String, requestListener: ParsedRequestListener<MutableList<NotificationEntity>>) {
//        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsObjectList(NotificationEntity::class.java, requestListener)
    }

    fun putNotificationClear(url: String, context: Context, requestListener: StringRequestListener) {
        val token = UserRepository(context).getUser()!!.token
        AndroidNetworking.put(url)
            .addHeaders("Authorization", token)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsString(requestListener)
    }

    fun deleteNotificationById(url: String, requestListener: StringRequestListener) {
        AndroidNetworking.delete(url)
//                .setContentType("application/json")
            .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
            .build()
            .getAsString(requestListener)
    }

    fun setNotificationsSeen(
        url: String, notificationIds: MutableList<String>, token: String,
        requestListener: ParsedRequestListener<NotificationReadedResponse>
    ) {
        val jSon = Gson().toJson(notificationIds)
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addPathParameter("notifications", jSon)
            .addUrlEncodeFormBodyParameter("notifications", jSon)
            .addQueryParameter("notifications", jSon)
            .addBodyParameter(jSon)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(NotificationReadedResponse::class.java, requestListener)
    }

    fun setNotificationClicked(
        url: String, notification: NotificationEntity, token: String,
        requestListener: ParsedRequestListener<NotificationEntity>
    ) {
        val jSon = Gson().toJson(notification)
        AndroidNetworking.put(url + "/" + notification.id)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(jSon)
            .addBodyParameter(notification)
            .build()
            .getAsObject(NotificationEntity::class.java, requestListener)
    }

    fun putFireBaseToken(url: String, token: String, fireBaseToken: String, requestListener: StringRequestListener) {
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addBodyParameter("token", fireBaseToken)
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(requestListener)
    }


    fun getUserCV(url: String, requestListener: ParsedRequestListener<User>) {
        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(User::class.java, requestListener)
    }

    fun getTags(url: String, keyword: String, requestListener: ParsedRequestListener<MutableList<Tag>>) {

        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .addQueryParameter("filter", "{\"where\":{\"name\":{\"like\":\"$keyword\",\"options\":\"i\"}}}")
            .build()
            .getAsObjectList(Tag::class.java, requestListener)
    }

    fun postTag(
        url: String,
        tag: Tag,
        token: String,
        requestListener: ParsedRequestListener<Tag>
    ) {
        AndroidNetworking.post(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addBodyParameter(tag)
            .build()
            .getAsObject(Tag::class.java, requestListener)
    }


    fun updateProfile(
        url: String,
        updateProfile: SentUpdateProfile,
        token: String,
        requestListener: ParsedRequestListener<User?>
    ) {
        val jSon = Gson().toJson(updateProfile)
        AndroidNetworking.put(url)
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
            .build()
            .getAsObject(User::class.java, requestListener)
    }


    fun uploadCV(
        url: String,
        file: File,
        uploadProgressListener: UploadProgressListener,
        requestListener: ParsedRequestListener<MutableList<AttachmentResponse>>
    ) {
        if (file.exists()) {
            Log.v("Uploaded File", "Existed")
        }
        AndroidNetworking.upload(url)
            .addMultipartFile("file", file)
            .addMultipartParameter("key", "value")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener(uploadProgressListener)
            .getAsObjectList(AttachmentResponse::class.java, requestListener)

        //?filter={"include":["CV"]}
        /*Notification ended*/

    }

    fun searchJobs(
        url: String,
        filtration: Map<String, String?>,
        requestListener: ParsedRequestListener<MutableList<Job>>
    ) {
        AndroidNetworking.get(url)
            .let {
                filtration.forEach { (key, value) ->
                    it.addQueryParameter(key, value)
                }
                it
            }
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(Job::class.java, requestListener)
    }


    fun getJobsByBusiness(
        url: String,
        businessId: String,
        requestListener: ParsedRequestListener<MutableList<Job>>
    ) {
        AndroidNetworking.get(url)
            .addQueryParameter("filter[where][businessId]=", businessId)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(Job::class.java, requestListener)
    }


    fun getJobDetails(
        jobId: String,
        token: String,
        requestListener: ParsedRequestListener<JobDetails>
    ) {
        AndroidNetworking.get("${ServerInfo.jobsUrl}$jobId${ServerInfo.jobDetailsUrl}")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(JobDetails::class.java, requestListener)
    }


    fun updateJobDetails(
        job: JobDetailsSent,
        jobId: String,
        token: String,
        requestListener: ParsedRequestListener<JobDetails>
    ) {
        val jSon = Gson().toJson(job)
        AndroidNetworking.put("${ServerInfo.jobsUrl}$jobId${ServerInfo.updateJobDetailsUrl}")
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
            .build()
            .getAsObject(JobDetails::class.java, requestListener)
    }


    fun addNewJob(
        job: JobDetailsSent,
        businessId: String,
        token: String,
        requestListener: ParsedRequestListener<JobDetails>
    ) {
        val jSon = Gson().toJson(job)
        AndroidNetworking.post("${ServerInfo.businessGuideUrl}/$businessId${ServerInfo.addNewJob}")
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
            .build()
            .getAsObject(JobDetails::class.java, requestListener)
    }


    fun getJobApplicants(
        jobId: String,
        token: String,
        requestListener: ParsedRequestListener<MutableList<Applicant>>
    ) {

        AndroidNetworking.get("${ServerInfo.jobsUrl}$jobId${ServerInfo.jobApplicants}")
            .setPriority(Priority.HIGH)
            .addHeaders("Authorization", token)
            .build()
            .getAsObjectList(Applicant::class.java, requestListener)
    }


    fun deactivateJob(
        jobStatus: JobStatus,
        jobId: String,
        token: String,
        requestListener: ParsedRequestListener<JobDetails>
    ) {
        val jSon = Gson().toJson(jobStatus)
        AndroidNetworking.patch("${ServerInfo.jobsUrl}$jobId")
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .addStringBody(jSon)
            .build()
            .getAsObject(JobDetails::class.java, requestListener)
    }

    fun getJobCategories(
        url: String,
        filtration: Map<String, String>,
        requestListener: ParsedRequestListener<MutableList<JobCategory>>
    ) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
            .addQueryParameter("filter", criteria)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(JobCategory::class.java, requestListener)
    }


    fun getJobTags(
        url: String,
        jobId: String,
        requestListener: ParsedRequestListener<MutableList<Tag>?>
    ) {

        AndroidNetworking.get("$url$jobId/tags")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObjectList(Tag::class.java, requestListener)
    }


    fun applyToJob(
        url: String,
        jobId: String,
        token: String,
        requestListener: ParsedRequestListener<Applicant?>
    ) {

        AndroidNetworking.post("$url$jobId/applyJobOpportunity")
            .addHeaders("Authorization", token)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(Applicant::class.java, requestListener)
    }


    fun updateApplicantStatus(
        url: String,
        applicationId: String,
        applicantStatus: ApplicantStatus,
        token: String,
        requestListener: ParsedRequestListener<Applicant?>
    ) {
        val jSon = Gson().toJson(applicantStatus)
        AndroidNetworking.put("$url$applicationId/changeStatus")
            .setContentType("application/json")
            .addHeaders("Authorization", token)
            .addStringBody(jSon)
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(Applicant::class.java, requestListener)
    }


}