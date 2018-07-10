package com.brainsocket.globalpages.api

import com.brainsocket.globalpages.data.entitiesModel.LoginModel
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.ParsedRequestListener
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.data.entitiesResponses.LoginResponse
import com.brainsocket.globalpages.data.entities.Volume
import com.brainsocket.globalpages.data.entitiesModel.ForgotPasswordModel
import com.brainsocket.globalpages.data.entitiesModel.SignupModel
import com.google.gson.Gson


/**
 * Created by Adhamkh on 2018-06-15.
 */
class ApiService {

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

    fun postUserSignUp(url: String, signupModel: SignupModel, requestListener: ParsedRequestListener<User>) {
        AndroidNetworking.post(url)
                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addBodyParameter(signupModel)
                .build()
                .getAsObject(User::class.java, requestListener)
    }

    fun getVolumes(url: String, filteration: Map<String, String>, requestListener: ParsedRequestListener<MutableList<Volume>>) {
        var criteria = Gson().toJson(filteration)
        AndroidNetworking.get(url)
//                .setContentType("application/json")
                .setPriority(Priority.HIGH)
                .addQueryParameter("filter", criteria)
                .build()
                .getAsObjectList(Volume::class.java, requestListener)
    }


}