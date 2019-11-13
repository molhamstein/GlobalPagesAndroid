package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.almersal.android.R
import com.almersal.android.adapters.ApplicantsAdapter
import com.almersal.android.data.entities.Applicant
import com.almersal.android.data.entities.ApplicantStatus
import com.almersal.android.di.component.DaggerApplicantsComponent
import com.almersal.android.di.module.ApplicantsModule
import com.almersal.android.di.ui.ApplicantsContract
import com.almersal.android.di.ui.ApplicantsPresenter
import kotlinx.android.synthetic.main.activity_applicants.*
import javax.inject.Inject

class ApplicantsActivity : BaseActivity(), ApplicantsContract.View {

    companion object {
        const val job_intent_key = "Job_intent_key"

    }

    @Inject
    lateinit var presenter: ApplicantsPresenter

    lateinit var jobId: String
    private var adapterPos = -1

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_applicants)

        jobId = intent.getStringExtra(job_intent_key)

        val component = DaggerApplicantsComponent.builder()
            .applicantsModule(ApplicantsModule(this))
            .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.getJobApplicants(jobId)
    }


    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            applicants.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            applicants.visibility = View.VISIBLE

        }
    }

    override fun onApplicantsLoaded(applicantsData: MutableList<Applicant>) {
        applicants.layoutManager = LinearLayoutManager(this)
        applicants.adapter = ApplicantsAdapter(this, applicantsData)
    }

    override fun onStatusChanged(success: Boolean, applicantId: String?) {
        if (success) {
            Toast.makeText(this, getString(R.string.status_success), Toast.LENGTH_SHORT).show()
        } else {

            Toast.makeText(this, getString(R.string.status_failed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun updateStatus(applicantStatus: ApplicantStatus, applicantId: String) {
        presenter.updateApplicantStatus(applicantId, applicantStatus)
    }


}
