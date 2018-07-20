package com.brainsocket.globalpages.utilities

import android.content.Context
import android.content.Intent
import com.brainsocket.globalpages.activities.*

/**
 * Created by Adhamkh on 2018-06-08.
 */
class intentHelper {

    companion object {
        fun startMainActivity(context: Context) {
            var intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startSignInActivity(context: Context) {
            var intent = Intent(context, SigninActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startSignUpActivity(context: Context) {
            var intent = Intent(context, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startForgotPasswordActivity(context: Context) {
            var intent = Intent(context, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessAddActivity(context: Context) {
            var intent = Intent(context, BusinessGuideAddActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessGuideDetailsActivity(context: Context) {
            var intent = Intent(context, BusinessGuideDetailsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startPostAddActivity(context: Context, userId: String = "-1") {
            var intent = Intent(context, PostAddActivity::class.java)
            intent.putExtra(PostAddActivity.USER_ID_TAG, userId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


        fun startPostDetailsActivity(context: Context, postId: String) {
            var intent = Intent(context, PostDetailsActivity::class.java)
            intent.putExtra(PostDetailsActivity.PostId_Tag, postId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startPostSearchFilterActivity(context: Context) {
            var intent = Intent(context, PostSearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startProfileActivity(context: Context) {
            var intent = Intent(context, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startBusinessGuideActivity(context: Context) {
            var intent = Intent(context, BusinessGuideSearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startNearByPharmaciesActivity(context: Context) {
            var intent = Intent(context, PharmacyNearByActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        fun startDutyPharmacyActivity(context: Context) {
            var intent = Intent(context, PharmacyDutySearchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }
}