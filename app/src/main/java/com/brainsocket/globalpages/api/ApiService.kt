package com.brainsocket.globalpages.api

import com.brainsocket.globalpages.data.entitiesModel.LoginModel
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.data.entitiesModel.ForgotPasswordModel
import com.brainsocket.globalpages.data.entitiesModel.SignUpModel
import com.google.gson.Gson


/**
 * Created by Adhamkh on 2018-06-15.
 */
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

    fun getUserPosts(url: String, filteration: MutableMap<String, Pair<String, String>>, requestListener: ParsedRequestListener<MutableList<Post>>) {
        val criteria = Gson().toJson(filteration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(Post::class.java, requestListener)
    }

    fun getUserBusinesses(url: String, filteration: MutableMap<String, Pair<String, String>>, requestListener: ParsedRequestListener<MutableList<BusinessGuide>>) {
        val criteria = Gson().toJson(filteration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(BusinessGuide::class.java, requestListener)
    }


    /*User Profile ended*/

    /*Tagging started*/
    fun getBusinessCategories(url: String, requestListener: ParsedRequestListener<MutableList<BusinessGuideCategory>>) {
        AndroidNetworking.get(url)
//              .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(BusinessGuideCategory::class.java, requestListener)
    }

    fun getPostCategories(url: String, requestListener: ParsedRequestListener<MutableList<PostCategory>>) {
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(PostCategory::class.java, requestListener)
    }

    fun getCities(url: String, requestListener: ParsedRequestListener<MutableList<City>>) {
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(City::class.java, requestListener)
    }
    /*Tagging ended*/


    /*Volumes started*/
    fun getVolumes(url: String, filteration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<Volume>>) {
        val criteria = Gson().toJson(filteration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(Volume::class.java, requestListener)
    }
    /*Volumes ended*/


    /*Business Guides started*/
    fun getBusinessGuides(url: String, filteration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<BusinessGuide>>) {
        var criteria = Gson().toJson(filteration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(BusinessGuide::class.java, requestListener)
    }

    fun postBusinessGuides(url: String, businessGuide: BusinessGuide, requestListener: StringRequestListener) {
        AndroidNetworking.post(url)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addBodyParameter(businessGuide)
                .build()
                .getAsString(requestListener)
    }

    /*Business Guides ended*/


}