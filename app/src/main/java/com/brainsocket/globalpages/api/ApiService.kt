package com.brainsocket.globalpages.api

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.google.gson.Gson
import java.io.File
import com.androidnetworking.interfaces.*
import com.brainsocket.globalpages.data.entitiesModel.*
import com.brainsocket.globalpages.data.entitiesResponses.AttachmentResponse
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

    fun postUserForgotPassword(url: String, forgotPasswordModel: ForgotPasswordModel, requestListener: JSONObjectRequestListener) {
        AndroidNetworking.post(url)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addApplicationJsonBody(forgotPasswordModel.email)
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

    fun getUserPosts(url: String/*, filtration: MutableMap<String, Pair<String, String>>*/, requestListener: ParsedRequestListener<MutableList<Post>>) {
//        val criteria = Gson().toJson(filtration)

        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(Post::class.java, requestListener)
    }

    fun getUserBusinesses(url: String, /*filtration: MutableMap<String, Pair<String, String>>,*/ requestListener: ParsedRequestListener<MutableList<BusinessGuide>>) {
//        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(BusinessGuide::class.java, requestListener)
    }

    fun updateUserProfile(url: String, profile: ProfileModel, requestListener: StringRequestListener) {
        val filtration: MutableMap<String, Pair<String, String>> = HashMap()//[ownerId]
        filtration["where"] = Pair("id", profile.id!!)

        val criteria = Gson().toJson(filtration)
        AndroidNetworking.post(url)
                .setContentType("application/json")
                .addQueryParameter("filter", criteria)
                .setPriority(Priority.HIGH)
                .addBodyParameter(profile)
                .build()
                .getAsString(requestListener)
    }

    /*User Profile ended*/

    /*Tagging started*/
    fun getBusinessCategories(url: String, filtration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<BusinessGuideCategory>>) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//              .setContentType("application/json")
                .addQueryParameter("filter", criteria)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(BusinessGuideCategory::class.java, requestListener)
    }

    fun getPostCategories(url: String, filtration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<PostCategory>>) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .addQueryParameter("filter", criteria)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(PostCategory::class.java, requestListener)
    }

    fun getCities(url: String, filtration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<City>>) {
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
    fun getVolumes(url: String, filtration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<Volume>>) {
        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(Volume::class.java, requestListener)
    }
    /*Volumes ended*/


    /*Business Guides started*/
    fun getBusinessGuides(url: String, /*filtration: MutableMap<String, Pair<String, String>>,*/ requestListener: ParsedRequestListener<MutableList<BusinessGuide>>) {
//        val criteria = Gson().toJson(filtration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
//                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(BusinessGuide::class.java, requestListener)
    }

    fun postBusinessGuides(url: String, businessGuide: BusinessGuideModel, token: String, requestListener: StringRequestListener) {
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
                .getAsString(requestListener)
    }

    /*Business Guides ended*/

    /*Post started*/
    fun postPost(url: String, postModel: PostModel, token: String, requestListener: StringRequestListener) {
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
                .getAsString(requestListener)
    }


    /*Post ended*/

    /*Attachment started*/

    fun uploadAttachment(url: String, file: File,
                         uploadProgressListener: UploadProgressListener, requestListener: ParsedRequestListener<MutableList<AttachmentResponse>>) {
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
//                { bytesUploaded, totalBytes ->
//                   Log.v("","")
//                }
                .getAsObjectList(AttachmentResponse::class.java, requestListener)
//                (object : JSONObjectRequestListener {
//                    override fun onResponse(response: JSONObject) {
//                        Log.v("", "")
//                    }
//
//                    override fun onError(error: ANError) {
//                        Log.v("", "")
//                    }
//                })

    }

    /*Attachment ended*/


    /*Business Product started*/
    fun postBusinessGuideProduct(url: String, productThumbModel: ProductThumbModel, requestListener: StringRequestListener) {
        AndroidNetworking.post(url)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addBodyParameter(productThumbModel)
                .build()
                .getAsString(requestListener)
    }

    /*Business Product ended*/


}