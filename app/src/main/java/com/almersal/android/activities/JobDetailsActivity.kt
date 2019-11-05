package com.almersal.android.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.almersal.android.R
import com.almersal.android.adapters.SkillsAdapter
import com.almersal.android.data.entities.Job
import com.almersal.android.data.entities.Tag
import com.almersal.android.di.component.DaggerJobDetailsComponent
import com.almersal.android.di.module.JobDetailsModule
import com.almersal.android.di.ui.JobDetailsContract
import com.almersal.android.di.ui.JobDetailsPresenter
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.BindingUtils
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_job_details.*
import kotlinx.android.synthetic.main.activity_job_details.companyName
import kotlinx.android.synthetic.main.activity_job_details.location
import javax.inject.Inject


class JobDetailsActivity : BaseActivity(), JobDetailsContract.View, View.OnClickListener {

    companion object {
        const val job_intent_key = "Job_intent_key"
        const val edit_flag_key = "Edit_Flag"
    }

    var editFlag: Boolean = false
    var job: Job? = null

    @Inject
    lateinit var presenter: JobDetailsPresenter

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_job_details)
        val component = DaggerJobDetailsComponent.builder()
            .jobDetailsModule(JobDetailsModule(this))
            .build()
        component.inject(this)
        presenter.attachView(this)

        val jSon: String? = intent.getStringExtra(job_intent_key)
        editFlag = intent.getBooleanExtra(edit_flag_key, false)
        jSon?.let {
            job = Gson().fromJson(jSon, Job::class.java)
            bindView(job!!)

            presenter.getTags(job?.id!!)
        }
        editDetails(editFlag)

        apply.setOnClickListener(this)
        bottomApply.setOnClickListener(this)

    }


    fun editDetails(flag: Boolean) {
        if (flag) {
            editBtn.visibility = View.VISIBLE
            applyBtn.visibility = View.GONE
            deactivateBtn.visibility = View.VISIBLE
            bottomApplyBtn.visibility = View.GONE
            return
        }


        editBtn.visibility = View.GONE
        applyBtn.visibility = View.VISIBLE
        deactivateBtn.visibility = View.GONE
        bottomApplyBtn.visibility = View.VISIBLE
    }


    fun bindView(job: Job) {
        BindingUtils.loadBusinessImage(image, job.business.logo)
        positionTitle.text = job.name
        companyName.text = job.business.name
        location.text = job.business.city?.getTitle() + "," + job.business.location?.getTitle()
        date.text = DateNormalizer.getCustomFormate(job.creationDate, "MMM dd, yyyy")
        descriptionValue.text = job.description
        categoryName.text = job.category.getTitle()
        subCategoryName.text = job.subCategory.getTitle()
        qualificationsValue.text = job.qualifications
        responsibilitiesValue.text = job.responsibilities
        salaryValue.text = job.rangeSalary
        educationValue.text = job.minimumEducationLevel
        jobTypeValue.text = job.jobType


    }

    override fun onClick(v: View?) {
        presenter.applyToJob(job!!.id)
    }

    override fun showProgress(show: Boolean) {
        if(show){
            progress.visibility = View.VISIBLE
        }
        else{
            progress.visibility = View.GONE
        }
    }

    override fun showTagsProgress(show: Boolean) {
        if (show) {
            skills.visibility = View.GONE
            skillsProgress.visibility = View.VISIBLE
        } else {
            skills.visibility = View.VISIBLE
            skillsProgress.visibility = View.GONE
        }
    }

    override fun onTagsLoaded(tags: MutableList<Tag>?) {
        val skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        skills.layoutManager = skillLayoutManager
        skills.adapter = SkillsAdapter(this, tags!!, false)
    }

    override fun onApplySuccess(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun appliedBefore() {
        Toast.makeText(this, getString(R.string.applied_before), Toast.LENGTH_SHORT).show()

    }

}
