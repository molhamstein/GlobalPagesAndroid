package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.City
import com.almersal.android.data.entities.Job
import com.almersal.android.data.entities.Tag
import com.almersal.android.repositories.DataStoreRepositories
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import io.reactivex.disposables.CompositeDisposable
import java.util.HashMap

class JobDetailsPresenter(val context: Context) : JobDetailsContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private lateinit var view: JobDetailsContract.View

    override fun attachView(view: JobDetailsContract.View) {
        this.view = view
    }


    override fun subscribe() {
    }

    override fun unSubscribe() {
    }


    override fun getTags(jobId: String) {

        view.showTagsProgress(true)


        ApiService().getJobTags(ServerInfo.jobsUrl, jobId, object : ParsedRequestListener<MutableList<Tag>?> {
            override fun onResponse(response: MutableList<Tag>?) {
                view.showTagsProgress(false)
                if (response != null) {
                    view.onTagsLoaded(response)
                }
            }

            override fun onError(anError: ANError?) {
                view.showTagsProgress(false)
            }
        })


    }

    override fun applyToJob(jobId: String) {
        view.showProgress(true)


        ApiService().applyToJob(ServerInfo.jobUserUrl, jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<String?> {
                override fun onResponse(response: String?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onApplySuccess(response)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    if(anError?.errorCode == 600)
                        view.appliedBefore()
                }
            })
    }

}