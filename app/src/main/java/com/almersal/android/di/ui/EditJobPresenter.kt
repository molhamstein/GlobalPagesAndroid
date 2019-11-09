package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.R
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.JobDetails
import com.almersal.android.data.entities.JobDetailsSent
import com.almersal.android.data.entities.Tag
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import com.google.gson.JsonObject


class EditJobPresenter constructor(val context: Context) : EditJobContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: EditJobContract.View

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }


    override fun attachView(view: EditJobContract.View) {
        this.view = view
    }


    override fun getTags(keyword: String) {
        view.showAutoCompleteProgress(true)


        ApiService().getTags(ServerInfo.tagsUrl, keyword, object : ParsedRequestListener<MutableList<Tag>> {
            override fun onResponse(response: MutableList<Tag>?) {
                view.showAutoCompleteProgress(false)
                if (response != null) {
                    view.onTagsLoaded(response)
                } else {
                    view.onTagsLoaded(mutableListOf())
                }
            }

            override fun onError(anError: ANError?) {
                view.showAutoCompleteProgress(false)
                view.onTagsLoaded(mutableListOf())
            }
        })
    }

    override fun addTag(tag: Tag?) {
        if (tag != null) {
            view.showAutoCompleteProgress(true)

            ApiService().postTag(
                ServerInfo.tagsUrl,
                tag,
                UserRepository(context).getUser()!!.token,
                object : ParsedRequestListener<Tag> {
                    override fun onResponse(response: Tag?) {
                        view.showAutoCompleteProgress(false)
                        if (response != null) {
                            view.onTagAdded(tag)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        view.showAutoCompleteProgress(false)
                        view.showLoadErrorMessage(true)
                    }
                })
        }
    }


    override fun getJobDetails(jobId: String) {
        view.showProgress(true)

        ApiService().getJobDetails(jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<JobDetails> {
                override fun onResponse(response: JobDetails) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onJobDetailsLoaded(response)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                }
            })
    }

    override fun editJob(job: JobDetailsSent, jobId: String) {
        view.showProgress(true)

        ApiService().updateJobDetails(job, jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<JobDetails> {
                override fun onResponse(response: JobDetails) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onJobEdited(response, true, context.getString(R.string.job_edited))
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    val errorBody = Gson().fromJson(anError?.errorBody, JsonObject::class.java)
                    view.onJobEdited(
                        JobDetails(),
                        false,
                        errorBody.get("error").asJsonObject.get("message").asString
                    )
                }
            })
    }
}