package com.almersal.android.di.ui

import com.almersal.android.data.entities.Applicant
import com.almersal.android.data.entities.ApplicantStatus

class ApplicantsContract {

    interface Presenter : BaseContract.Presenter<View> {

        fun getJobApplicants(jobId: String)
        fun updateApplicantStatus(applicationId: String,status: ApplicantStatus)


    }

    interface View : BaseContract.View {

        fun onApplicantsLoaded(applicants: MutableList<Applicant>)
        fun onStatusChanged(success: Boolean, applicantId: String?)
        fun updateStatus(applicantStatus: ApplicantStatus, applicantId: String)

    }

}