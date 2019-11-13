package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.*
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import io.reactivex.disposables.CompositeDisposable

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


    override fun applyToJob(jobId: String) {
        view.showProgress(true)


        ApiService().applyToJob(ServerInfo.jobUserUrl, jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<Applicant?> {
                override fun onResponse(response: Applicant?) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onApplySuccess(response)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    if (anError?.errorCode == 600)
                        view.appliedBefore()
                }
            })
    }

    override fun getJobDetails(jobId: String) {
        view.showProgress(true)

        ApiService().getJobDetails(jobId, UserRepository(context).getUser()?.token ?:"",
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


    override fun deactivateJob(jobId: String, jobStatus: JobStatus) {
        view.showProgress(true)

        ApiService().deactivateJob(jobStatus, jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<JobDetails> {
                override fun onResponse(response: JobDetails) {
                    view.showProgress(false)
                    if (response != null) {
                        view.onJobDeactivated(response)
                    }
                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                }
            })
    }


}