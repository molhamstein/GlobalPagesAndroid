package com.almersal.android.di.ui

import android.content.Context
import com.almersal.android.api.ApiService
import com.almersal.android.api.ServerInfo
import com.almersal.android.data.entities.Applicant
import com.almersal.android.data.entities.ApplicantStatus
import com.almersal.android.repositories.UserRepository
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import io.reactivex.disposables.CompositeDisposable

class ApplicantsPresenter(val context: Context) : ApplicantsContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: ApplicantsContract.View

    override fun attachView(view: ApplicantsContract.View) {
        this.view = view
    }


    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun getJobApplicants(jobId: String) {
        view.showProgress(true)


        ApiService().getJobApplicants(
            jobId, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<MutableList<Applicant>> {
                override fun onResponse(response: MutableList<Applicant>) {
                    view.showProgress(false)
                    view.onApplicantsLoaded(response)

                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                }
            })
    }

    override fun updateApplicantStatus(applicationId: String, status: ApplicantStatus) {
        view.showProgress(true)


        ApiService().updateApplicantStatus(
            ServerInfo.jobUserUrl, applicationId, status, UserRepository(context).getUser()!!.token,
            object : ParsedRequestListener<Applicant?> {
                override fun onResponse(response: Applicant?) {
                    view.showProgress(false)
                    view.onStatusChanged(true, null)

                }

                override fun onError(anError: ANError?) {
                    view.showProgress(false)
                    view.onStatusChanged(false, null)
                }
            })
    }
}