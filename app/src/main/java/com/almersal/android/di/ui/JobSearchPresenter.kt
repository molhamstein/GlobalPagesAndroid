package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.Job
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import io.reactivex.disposables.CompositeDisposable

class JobSearchPresenter(val context: Context) : JobSearchContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: JobSearchContract.View


    override fun searchJobs(filters: Map<String, String?>) {
        view.showProgress(true)
        ApiService().searchJobs(ServerInfo.jobSearchUrl, filters, object : ParsedRequestListener<MutableList<Job>> {
            override fun onResponse(response: MutableList<Job>?) {

                if ((response != null)) {
                    view.showProgress(false)
                    if (response.size > 0) {
                        view.onJobsLoaded(response)
                        return
                    }
                }
                view.showEmptyView(true)
            }

            override fun onError(anError: ANError?) {
                view.showProgress(false)
                view.showLoadErrorMessage(true)
            }
        })
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun attachView(view: JobSearchContract.View) {
        this.view = view
    }

}