package com.almersal.android.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Adapter
import com.almersal.android.R
import com.almersal.android.adapters.JobsSearchAdapter
import com.almersal.android.adapters.TagsRecyclerViewAdapter
import com.almersal.android.data.entities.Job
import com.almersal.android.data.entities.JobCategory
import com.almersal.android.data.entities.PointEntity
import com.almersal.android.data.entities.TagEntity
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.di.component.DaggerJobSearchComponenet
import com.almersal.android.di.module.JobSearchModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.JobSearchContract
import com.almersal.android.di.ui.JobSearchPresenter
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.enums.FilterType
import com.almersal.android.enums.TagType
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnTagSelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.utilities.IntentHelper
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_jobs_search.*
import javax.inject.Inject

class JobsSearchActivity : BaseActivity(), JobSearchContract.View, View.OnClickListener, OnTagSelectListener {


    @Inject
    lateinit var presenter: JobSearchPresenter


    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: JobsSearchAdapter


    var filter: MutableMap<String, String?> = mutableMapOf()

    var skip = 0
    var limit = 10
    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_jobs_search)
        val component = DaggerJobSearchComponenet.builder()
            .jobSearchModule(JobSearchModule(this))
            .build()
        component.inject(this)


        presenter.attachView(this)


        adapter = JobsSearchAdapter(this, mutableListOf())
        layoutManager = LinearLayoutManager(this)
        jobsRecycler.layoutManager = layoutManager
        jobsRecycler.adapter = adapter

        loadData(true)
        selectedTagsView.setOnClickListener(this)
        searchBtn.setOnClickListener(this)

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummyDataRepositories.getTagsDefaultRepositories()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this
        backBtn.setOnClickListener(this)

        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.Job_Filter_Activity_Tag -> {
                    setFilterEntity(it.message as FilterEntity)
                }
            }
        }

    }


    override fun onJobsLoaded(jobs: MutableList<Job>) {
        adapter.addData(jobs)
    }

    override fun onClick(v: View?) {
        when (v) {
            selectedTagsView, searchBtn -> {
                val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
                IntentHelper.startPostSearchFilterActivityForResult(
                    activity = this,
                    tagList = list,
                    filter = FilterType.JobsFilter
                )

            }
            backBtn -> {
                onBackPressed()
            }
        }
    }


    override fun onTagClick(tagEntity: TagEntity) {
        val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(
            activity = this,
            tagList = list,
            filter = FilterType.JobsFilter
        )

    }

    private fun setFilterEntity(filterEntity: FilterEntity) {

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(baseContext, filterEntity.getTags()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this
        filter["categoryId"] = filterEntity.category?.id
        filter["subCategoryId"] = filterEntity.subCategory?.id
        filter["cityId"] = filterEntity.city?.id
        filter["keyword"] = filterEntity.query

        loadData(true)


    }


    private fun loadData(clearFlag: Boolean) {
        if (clearFlag) adapter.clear()
        filter["offset"] = skip.toString()
        filter["limit"] = limit.toString()

        presenter.searchJobs(filter)
    }

    override fun onSelectTag(tagEntity: TagEntity) {


        when (tagEntity.tagType) {
            TagType.Category -> {
                filter["categoryId"] = null
            }
            TagType.SubCategory -> {
                filter["subCategoryId"] = null
            }
            TagType.City -> {
                filter["cityId"] = null
            }
            TagType.Query -> {
                filter["keyword"] = null
            }
        }

        loadData(true)


    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE

        } else {
            progressBar.visibility = View.GONE
        }
    }


}
