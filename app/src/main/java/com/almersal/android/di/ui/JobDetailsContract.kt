package com.almersal.android.di.ui

import com.almersal.android.data.entities.*


class JobDetailsContract {
    interface Presenter : BaseContract.Presenter<View> {
        //        fun getTags(jobId: String)
        fun applyToJob(jobId: String)

        fun getJobDetails(jobId: String)

        //        fun getJobApplicants(jobId: String)
        fun deactivateJob(jobId: String, jobStatus: JobStatus)
    }

    interface View : BaseContract.View {

        fun onApplySuccess(applyJobResponse: Applicant)
        fun onJobDetailsLoaded(jobDetails: JobDetails)
        fun onUpdateDetailsSuccess(jobDetails: JobDetails)

        fun onJobDeactivated(jobDetails: JobDetails)
        fun appliedBefore()

    }

}