package com.almersal.android.di.ui

import com.almersal.android.data.entities.Tag


class JobDetailsContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun getTags(jobId: String)
        fun applyToJob(jobId:String)

    }

    interface View : BaseContract.View {
        fun showTagsProgress(show: Boolean)
        fun onTagsLoaded(tags: MutableList<Tag>?)

        fun onApplySuccess(msg:String)
        fun appliedBefore()

    }

}