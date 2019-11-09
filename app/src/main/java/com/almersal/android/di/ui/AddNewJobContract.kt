package com.almersal.android.di.ui

import com.almersal.android.data.entities.JobDetails
import com.almersal.android.data.entities.JobDetailsSent
import com.almersal.android.data.entities.Tag

class AddNewJobContract {

    interface Presenter : BaseContract.Presenter<View> {
        fun getTags(keyword: String)
        fun addTag(tag: Tag?)
        fun addNewJob(job: JobDetailsSent, businessId: String)
    }

    interface View : BaseContract.View {
        fun onTagsLoaded(tags: List<Tag>)
        fun onTagAdded(tag: Tag)
        fun onJobAddedSuccess(
            job: JobDetails,
            flag: Boolean,
            msg: String?
        )
        fun showAutoCompleteProgress(flag: Boolean)
    }
}