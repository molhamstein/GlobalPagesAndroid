package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.almersal.android.App
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideRecyclerViewAdapter
import com.almersal.android.adapters.BusinessSelectorAdapter
import com.almersal.android.adapters.SkillsAdapter
import com.almersal.android.adapters.SkillsAutoCompleteAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerAddNewJobComponent
import com.almersal.android.di.module.AddNewJobModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.AddNewJobContract
import com.almersal.android.di.ui.AddNewJobPresneter
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.enums.EducationLevel
import com.almersal.android.enums.JobTypes
import com.almersal.android.listeners.OnBusinessSelectListener
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.AnalyticsEvents
import com.almersal.android.utilities.EnumsProvider
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_add_new_job.*
import kotlinx.android.synthetic.main.activity_add_new_job.progress
import kotlinx.android.synthetic.main.skills_edit_dialog.*


import javax.inject.Inject

class AddNewJobActivity : BaseActivity(), View.OnClickListener, AddNewJobContract.View,
    TagsCollectionContact.View,
    AdapterView.OnItemSelectedListener, OnBusinessSelectListener {


    companion object {
        const val businessId_key = "businessId"
    }

    lateinit var skillsDialog: AlertDialog
    lateinit var skillsAutoCompleteAdapter: SkillsAutoCompleteAdapter
    lateinit var dialogSkillsAdapter: SkillsAdapter
    var skillsData: MutableList<Tag>? = null

    lateinit var skillsAdapter: SkillsAdapter
    lateinit var skillLayoutManager: FlexboxLayoutManager

    var businessId: String? = null

    @Inject
    lateinit var presenter: AddNewJobPresneter

    @Inject
    lateinit var tagsCollectionPresenter: TagsCollectionPresenter

    private var jobDetails: JobDetailsSent = JobDetailsSent()


    private var categoriesData: MutableList<JobCategory> = mutableListOf()
    private var subCategoriesData: MutableList<SubCategory> = mutableListOf()


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_add_new_job)

        val component = DaggerAddNewJobComponent.builder()
            .addNewJobModule(AddNewJobModule(this))
            .tagsCollectionModule(TagsCollectionModule(this))
            .build()
        component.inject(this)

        mFirebaseAnalytics.logEvent(AnalyticsEvents.ADD_JOB_OPENED, null)
        businessId = intent.getStringExtra(businessId_key)
        if (businessId != null) {
            businesses.visibility = View.GONE
        } else {
            businesses.visibility = View.VISIBLE
        }

        init()

        presenter.attachView(this)
        presenter.loadUserBusinesses(UserRepository(App.app).getUser()!!.id!!)

        tagsCollectionPresenter.attachView(this)
        tagsCollectionPresenter.getJobCategories(true)


    }


    fun init() {
        skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        skillsAdapter = SkillsAdapter(this, mutableListOf(), false)
        skillsMain.layoutManager = skillLayoutManager
        skillsMain.adapter = skillsAdapter

        skillsDialog =
            AlertDialog.Builder(this)
                .setView(layoutInflater.inflate(R.layout.skills_edit_dialog, null))
                .create()



        educationLevel.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                EnumsProvider.educationLevels.map { it.level })
        jobType.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                EnumsProvider.jobTypes.map { it.type })


        jobDetails.jobType = EnumsProvider.jobTypes.map { it.name }[0]
        jobDetails.minimumEducationLevel = EnumsProvider.educationLevels.map { it.name }[0]

        editSkillsBtn.setOnClickListener(this)
        submitBtn.setOnClickListener(this)

        category.onItemSelectedListener = this
        subCategory.onItemSelectedListener = this
        educationLevel.onItemSelectedListener = this
        jobType.onItemSelectedListener = this
    }

    override fun onClick(v: View?) {
        when (v) {
            editSkillsBtn -> {
                openSkillEditDialog()
            }
            submitBtn -> {
                submitData()
                if (!businessId.isNullOrEmpty())
                    presenter.addNewJob(jobDetails, businessId!!)
                else
                    Toast.makeText(this, "Business Id is not provided", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun submitData() {
        with(jobDetails) {
            nameAr = nameArInput.text.toString()
            nameEn = nameEnInput.text.toString()
            qualificationsAr = qualificationsArInput.text.toString()
            qualificationsEn = qualificationsEnInput.text.toString()
            responsibilitiesAr = responsibilitiesArInput.text.toString()
            responsibilitiesEn = responsibilitiesEnInput.text.toString()
            descriptionAr = descriptionArInput.text.toString()
            descriptionEn = descriptionEnInput.text.toString()
            rangeSalary = salaryInput.text.toString()
            ownerId = UserRepository(App.app).getUser()!!.id!!
            businessId = this@AddNewJobActivity.businessId
        }

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            parent?.id == category.id -> {
                subCategoriesData = categoriesData[position].subCategories
                jobDetails.category = categoriesData[position]
                jobDetails.categoryId = categoriesData[position].id

                subCategory.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    subCategoriesData.map { it.getTitle() }
                )
            }
            parent?.id == subCategory.id -> {
                jobDetails.subCategory = subCategoriesData[position]
                jobDetails.subCategoryId = subCategoriesData[position].id
            }
            parent?.id == educationLevel.id -> {
                jobDetails.minimumEducationLevel = EnumsProvider.educationLevels[position].name

            }
            parent?.id == jobType.id -> {
                jobDetails.jobType = EnumsProvider.jobTypes[position].name

            }
        }
    }

    fun openSkillEditDialog() {

        skillsDialog.show()

        with(skillsDialog) {
            val tempSkills = mutableListOf<Tag>()
            skillsData?.let { for (tag in it) tempSkills.add(tag) }

            dialogSkillsAdapter = SkillsAdapter(this@AddNewJobActivity, tempSkills, true)

            var dialogSkillLayoutManager = FlexboxLayoutManager(this@AddNewJobActivity)
            dialogSkillLayoutManager.alignItems = AlignItems.BASELINE
            dialogSkillLayoutManager.flexWrap = FlexWrap.WRAP

            skills.layoutManager = dialogSkillLayoutManager
            skills.adapter = dialogSkillsAdapter

            var addedSkill: Tag? = null

            skillsAutoCompleteAdapter =
                SkillsAutoCompleteAdapter(
                    this@AddNewJobActivity,
                    android.R.layout.simple_dropdown_item_1line
                )
            skillsSearchInput.threshold = 1
            skillsSearchInput.setAdapter(skillsAutoCompleteAdapter)
            skillsSearchInput.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    addedSkill = skillsAutoCompleteAdapter.getTagAt(position)

                }

            skillsSearchInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    android.os.Handler().postDelayed({ presenter.getTags(s.toString()) }, 500)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })

            addSkillBtn.setOnClickListener {
                if (addedSkill == null) {
                    val searchText = skillsSearchInput.text.toString()
                    if (searchText.isNotEmpty()) {
                        val index =
                            skillsAutoCompleteAdapter.data.map { it.name }.indexOf(searchText)
                        if (index != -1) {
                            addedSkill = skillsAutoCompleteAdapter.getTagAt(index)
                            dialogSkillsAdapter.addItem(addedSkill)
                        } else
                            presenter.addTag(Tag(name = searchText))
                    }
                } else {
                    dialogSkillsAdapter.addItem(addedSkill)
                }
                addedSkill = null
                skillsSearchInput.setText("")


            }
            skillsDoneBtn.setOnClickListener {
                jobDetails.tags = tempSkills.map { it.id ?: "" }.toMutableList()
                skillsAdapter.data = tempSkills
                skillsAdapter.notifyDataSetChanged()
                dismiss()
            }

        }
    }


    override fun onTagsLoaded(tags: List<Tag>) {
        skillsAutoCompleteAdapter.data = tags.toMutableList()
        skillsAutoCompleteAdapter.notifyDataSetChanged()

    }


    override fun onTagAdded(tag: Tag) {
        dialogSkillsAdapter.addItem(tag)
    }

    override fun onJobAddedSuccess(job: JobDetails, flag: Boolean, msg: String?) {
        if (flag) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            onBackPressed()
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }


    override fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showAutoCompleteProgress(flag: Boolean) {
        skillsDialog.addSkillBtn.visibility = if (flag) View.GONE else View.VISIBLE
        skillsDialog.progress.visibility = if (flag) View.VISIBLE else View.GONE
    }


    override fun showJobCategoriesProgress(visible: Boolean) {
        showProgress(visible)
    }

    override fun onJobCategoriesLoaded(categoriesList: MutableList<JobCategory>) {
        categoriesData =
            categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList()
        category.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categoriesData.map { it.getTitle() }
        )

        jobDetails.category = categoriesData[0]
        jobDetails.categoryId = categoriesData[0].id


        subCategoriesData = categoriesData[0].subCategories
        subCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            subCategoriesData.map { it.getTitle() }
        )

        jobDetails.subCategory = subCategoriesData[0]
        jobDetails.subCategoryId = subCategoriesData[0].id

    }

    override fun onUserBusinessesListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {
        businesses.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businesses.adapter =
            BusinessSelectorAdapter(baseContext, businessGuideList, this)
    }

    override fun onSelectBusiness(business: BusinessGuide) {
        businessId = business.id
    }


}
