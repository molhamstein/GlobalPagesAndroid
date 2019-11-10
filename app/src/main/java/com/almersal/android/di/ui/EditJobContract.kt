package com.almersal.android.di.ui

import com.almersal.android.data.entities.JobDetails
import com.almersal.android.data.entities.JobDetailsSent
import com.almersal.android.data.entities.Tag

class EditJobContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun getTags(keyword: String)
        fun addTag(tag: Tag?)
        fun editJob(job: JobDetailsSent, businessId: String)
        fun getJobDetails(jobId: String)
    }

    interface View : BaseContract.View {
        fun onTagsLoaded(tags: List<Tag>)
        fun onTagAdded(tag: Tag)
        fun onJobEdited(
            job: JobDetails,
            flag: Boolean,
            msg: String?
        )
        fun onJobDetailsLoaded(jobDetails: JobDetails)
        fun showAutoCompleteProgress(flag: Boolean)
    }
}