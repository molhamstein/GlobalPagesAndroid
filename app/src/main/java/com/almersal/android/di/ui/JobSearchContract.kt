package com.almersal.android.di.ui

import com.almersal.android.data.entities.*

class JobSearchContract {
    interface Presenter : BaseContract.Presenter<View> {
        fun searchJobs(filters: Map<String, String?>)
    }

    interface View : BaseContract.View {
        fun onJobsLoaded(jobs: MutableList<Job>)
    }
}