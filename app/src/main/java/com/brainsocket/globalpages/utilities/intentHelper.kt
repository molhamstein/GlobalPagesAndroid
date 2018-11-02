package com.brainsocket.globalpages.utilities

import android.content.Context
import android.content.Intent
import android.graphics.Picture
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.brainsocket.globalpages.activities.*
import com.brainsocket.globalpages.data.entities.*
import com.google.gson.Gson
import android.support.v4.content.ContextCompat.startActivity
import com.brainsocket.globalpages.R


class IntentHelper {

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

        fun startBusinessGuideDetailsActivity(context: Context, businessGuide: BusinessGuide) {
            val intent = Intent(context, BusinessGuideDetailsActivity::class.java)
            val jSon = Gson().toJson(businessGuide)
            intent.putExtra(BusinessGuideDetailsActivity.BusinessGuideDetailsActivity_Tag, jSon)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessGuideEditActivity(context: Context, businessGuide: BusinessGuide) {
            val intent = Intent(context, BusinessGuideAddActivity::class.java)
            val jSon = Gson().toJson(businessGuide)
            intent.putExtra(BusinessGuideAddActivity.BusinessGuideAddActivity_Tag, jSon)
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
            intent.putExtra(PostDetailsActivity.Post_Tag, Gson().toJson(post))
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
            val intent = Intent(context, FindNearByActivity::class.java)
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

        fun startProfileEditActivity(context: Context) {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startCallNumber(context: Context, phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + phoneNumber)
            context.startActivity(intent)
        }

        fun startProductAddActivity(context: Context, businessGuide: BusinessGuide) {
            val intent = Intent(context, ProductAddActivity::class.java)
            val jSon = Gson().toJson(businessGuide)
            intent.putExtra(ProductAddActivity.ProductAddActivity_Tag, jSon)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startProductAddActivity(context: Context, businessGuide: BusinessGuide, productThumb: ProductThumb) {
            val intent = Intent(context, ProductAddActivity::class.java)
            val jSon = Gson().toJson(businessGuide)
            intent.putExtra(ProductAddActivity.ProductAddActivity_Tag, jSon)

            val jSonProduct = Gson().toJson(productThumb)
            intent.putExtra(ProductAddActivity.ProductAddActivity_Product_Tag, jSonProduct)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startProductDetailsActivity(context: Context, productThumb: ProductThumb) {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            val jSon = Gson().toJson(productThumb)
            intent.putExtra(ProductDetailsActivity.ProductManageActivity_Tag, jSon)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startPostEditActivity(context: Context, post: Post) {
            val intent = Intent(context, PostAddActivity::class.java)
            val jSon = Gson().toJson(post)
            intent.putExtra(PostAddActivity.PostAddActivity_Tag, jSon)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startLocationViewerActivity(context: Context, pointEntity: PointEntity) {
            val intent = Intent(context, LocationViewerActivity::class.java)
            val jSon = Gson().toJson(pointEntity)
            intent.putExtra(LocationViewerActivity.LocationViewerActivity_Point_Tag, jSon)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startSettingsActivity(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startPictureActivity(context: Context, url: String) {
            val intent = Intent(context, PictureActivity::class.java)
            intent.putExtra(PictureActivity.PictureActivity_Tag, url)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startNotificationActivity(context: Context) {
            val intent = Intent(context, NotificationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startEmailAddress(context: Context, email: String) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.type = "text/plain"
            intent.data = Uri.parse("mailto:" + email)
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")

            context.startActivity(Intent.createChooser(intent,
                    context.resources.getString(R.string.SendMail)))
        }

        fun startWebSite(context: Context, site: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(WebViewActivity.WebViewActivity_Tag, site)
            context.startActivity(intent)
        }

    }
}