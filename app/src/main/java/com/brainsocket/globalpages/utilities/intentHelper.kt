package com.brainsocket.globalpages.utilities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.brainsocket.globalpages.activities.*
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entities.TagEntity
import com.google.gson.Gson

/**
 * Created by Adhamkh on 2018-06-08.
 */
class intentHelper {

    companion object {
        fun startMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startSignInActivity(context: Context) {
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startSignUpActivity(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startForgotPasswordActivity(context: Context) {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessAddActivity(context: Context) {
            val intent = Intent(context, BusinessGuideAddActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessGuideDetailsActivity(context: Context) {
            val intent = Intent(context, BusinessGuideDetailsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startPostAddActivity(context: Context, userId: String = "-1") {
            val intent = Intent(context, PostAddActivity::class.java)
            intent.putExtra(PostAddActivity.USER_ID_TAG, userId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startPostDetailsActivity(context: Context, post: Post) {
            val intent = Intent(context, PostDetailsActivity::class.java)
            intent.putExtra(PostDetailsActivity.Post_Tag,Gson().toJson(post))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startPostSearchFilterActivity(context: Context, tagList: MutableList<TagEntity> = mutableListOf()) {
            val intent = Intent(context, PostSearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(PostSearchActivity.Suggestion_List_Tag, Gson().toJson(tagList.toTypedArray()))
            context.startActivity(intent)
        }

        fun startPostSearchFilterActivityForResult(activity: AppCompatActivity, tagList: MutableList<TagEntity> = mutableListOf()) {
            val intent = Intent(activity, PostSearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(PostSearchActivity.Suggestion_List_Tag, Gson().toJson(tagList.toTypedArray()))
            activity.startActivityForResult(intent, PostSearchActivity.PostSearchActivity_ResultCode)
        }

        fun startProfileActivity(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessGuideSearchActivity(context: Context) {
            val intent = Intent(context, BusinessGuideSearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startNearByPharmaciesActivity(context: Context) {
            val intent = Intent(context, PharmacyNearByActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startDutyPharmacyActivity(context: Context) {
            val intent = Intent(context, PharmacyDutySearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startLocationCheckActivity(context: Context, activityName: String) {
            val intent = Intent(context, LocationCheckActivity::class.java)
            intent.putExtra(LocationCheckActivity.Target_Tag, activityName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startActivityByName(context: Context, activityName: String) {
            try {
                val intent = Intent()
                intent.setClassName(context, activityName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (ex: Exception) {
                Log.v("", "")
                Log.v("", "")
            }
        }

    }
}