package com.almersal.android.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import com.almersal.android.R
import com.almersal.android.adapters.SkillsAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerJobDetailsComponent
import com.almersal.android.di.module.JobDetailsModule
import com.almersal.android.di.ui.JobDetailsContract
import com.almersal.android.di.ui.JobDetailsPresenter
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.AnalyticsEvents
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.EnumsProvider
import com.almersal.android.utilities.IntentHelper
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
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
    var job: JobDetails? = null
    lateinit var jobId: String

    @Inject
    lateinit var presenter: JobDetailsPresenter

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_job_details)
        val component = DaggerJobDetailsComponent.builder()
            .jobDetailsModule(JobDetailsModule(this))
            .build()
        component.inject(this)
        presenter.attachView(this)
        jobId = intent.getStringExtra(job_intent_key)
        val bundle = Bundle() ;
        bundle.putString("job_id",jobId)
        mFirebaseAnalytics.logEvent(AnalyticsEvents.JOB_DETAILS_OPENED,bundle)
        presenter.getJobDetails(jobId)

        editFlag = intent.getBooleanExtra(edit_flag_key, false)
        editDetails(editFlag)

        apply.setOnClickListener(this)
        bottomApply.setOnClickListener(this)
        deactivate.setOnClickListener(this)
        editBtn.setOnClickListener(this)
        applicantsBtn.setOnClickListener(this)
        applicantsNumber.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        presenter.getJobDetails(jobId)
    }


    fun editDetails(flag: Boolean) {
        if (flag) {
            editBtn.visibility = View.VISIBLE
            applyBtn.visibility = View.GONE
            deactivateBtn.visibility = View.VISIBLE
            bottomApplyBtn.visibility = View.GONE
            return
        }

        applicantsBtn.visibility = View.GONE
        applicantsNumber.visibility = View.GONE
        editBtn.visibility = View.GONE
        applyBtn.visibility = View.VISIBLE
        deactivateBtn.visibility = View.GONE
        bottomApplyBtn.visibility = View.VISIBLE
    }


    fun bindView(job: JobDetails) {
        BindingUtils.loadBusinessImage(image, job.business?.logo)
        positionTitle.text = job.name
        companyName.text = job.business?.name
        location.text = job.business?.city?.getTitle() + "," + job.business?.location?.getTitle()
        date.text = DateNormalizer.getCustomFormate(job.creationDate, "MMM dd, yyyy")


        descriptionValue.text = job.description
        descriptionValue.visibility = if (job.description.isNullOrEmpty()) View.GONE else View.VISIBLE
        descriptionText.visibility = if (job.description.isNullOrEmpty()) View.GONE else View.VISIBLE
        descriptionText.movementMethod = LinkMovementMethod.getInstance()
        categoryName.text = job.category?.getTitle()
        subCategoryName.text = job.subCategory?.getTitle()
        subCategoryName.visibility = if (job.subCategory?.getTitle().isNullOrBlank()) View.GONE else View.VISIBLE
        categoryName.visibility = if (job.category?.getTitle().isNullOrBlank()) View.GONE else View.VISIBLE


        qualificationsValue.text = job.qualifications
        qualificationsValue.visibility = if (job.qualifications.isNullOrEmpty()) View.GONE else View.VISIBLE
        qualificationsText.visibility = if (job.qualifications.isNullOrEmpty()) View.GONE else View.VISIBLE


        responsibilitiesValue.text = job.responsibilities
        responsibilitiesValue.visibility = if (job.responsibilities.isNullOrEmpty()) View.GONE else View.VISIBLE
        responsibilitiesText.visibility = if (job.responsibilities.isNullOrEmpty()) View.GONE else View.VISIBLE

        salaryValue.text = job.rangeSalary
        salaryValue.visibility = if (job.rangeSalary.isNullOrEmpty()) View.GONE else View.VISIBLE
        salaryText.visibility = if (job.rangeSalary.isNullOrEmpty()) View.GONE else View.VISIBLE

        educationValue.text =
            EnumsProvider.educationLevels.firstOrNull { job.minimumEducationLevel?.equals(it.name) ?: false }?.level
        educationValue.visibility = if (job.minimumEducationLevel.isNullOrEmpty()) View.GONE else View.VISIBLE
        educationText.visibility = if (job.minimumEducationLevel.isNullOrEmpty()) View.GONE else View.VISIBLE

        jobTypeValue.text = EnumsProvider.jobTypes.firstOrNull { job.jobType?.equals(it.name) ?: false }?.type
        jobTypeValue.visibility = if (job.jobType.isNullOrEmpty()) View.GONE else View.VISIBLE
        jobTypeText.visibility = if (job.jobType.isNullOrEmpty()) View.GONE else View.VISIBLE


        applicantsNumber.text = job.NumberOfApplicants?.toString()
        if (job.NumberOfApplicants ?: 0 == 0) {
            applicantsNumber.visibility = View.GONE
        }
        if (job.userIsApplied) {
            apply.text = getString(R.string.applied_before)
            bottomApply.text = apply.text
            apply.isEnabled = false
        }

        onTagsLoaded(job.tags)


    }

    override fun onClick(v: View?) {
        when (v) {
            apply, bottomApply -> {
                presenter.applyToJob(job?.id ?: "")
            }
            deactivate -> {
                presenter.deactivateJob(job?.id!!, Status("deactivated"))
            }

            applicantsBtn, applicantsNumber -> {
                if (job?.NumberOfApplicants ?: 0 > 0)
                    IntentHelper.startApplicantsActivity(this, job?.id!!)
            }


            editBtn -> {
                IntentHelper.startEditJobActivity(this, job!!)
            }
        }

    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.GONE
        }
    }


    fun onTagsLoaded(tags: List<Tag>?) {
        val skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        skills.layoutManager = skillLayoutManager
        skills.adapter = SkillsAdapter(this, tags?.let { tags.toMutableList() } ?: mutableListOf(), false)
        if ((skills.adapter as SkillsAdapter).data.isNotEmpty()) {
            skills.visibility = View.VISIBLE
            skillsText.visibility = View.VISIBLE
        }
    }

    override fun onJobDetailsLoaded(jobDetails: JobDetails) {
        job = jobDetails
        bindView(jobDetails)
    }

    override fun onUpdateDetailsSuccess(jobDetails: JobDetails) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onJobDeactivated(jobDetails: JobDetails) {
        Toast.makeText(this, getString(R.string.job_deactived), Toast.LENGTH_SHORT).show()
        onBackPressed()
    }

    override fun onApplySuccess(applyJobResponse: Applicant) {
        apply.text = getString(R.string.applied_before)
        bottomApply.text = apply.text

        Toast.makeText(this, getString(R.string.applied_success), Toast.LENGTH_SHORT).show()
    }

    override fun appliedBefore() {
        Toast.makeText(this, getString(R.string.applied_before), Toast.LENGTH_SHORT).show()

    }

}
