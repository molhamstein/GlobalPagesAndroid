package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.almersal.android.R
import com.almersal.android.adapters.SkillsAdapter
import com.almersal.android.adapters.SkillsAutoCompleteAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerEditJobComponent
import com.almersal.android.di.module.EditJobModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.*
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.EnumsProvider
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_edit_job.*

import kotlinx.android.synthetic.main.skills_edit_dialog.*
import kotlinx.android.synthetic.main.skills_edit_dialog.progress
import javax.inject.Inject
import kotlin.text.category

class EditJobActivity : BaseActivity(), View.OnClickListener, EditJobContract.View, TagsCollectionContact.View,
    AdapterView.OnItemSelectedListener {
    companion object {
        const val jobId_key = "jobId"
    }

    lateinit var skillsDialog: AlertDialog
    lateinit var skillsAutoCompleteAdapter: SkillsAutoCompleteAdapter
    lateinit var dialogSkillsAdapter: SkillsAdapter
    var skillsData: MutableList<Tag>? = null

    lateinit var skillsAdapter: SkillsAdapter
    lateinit var skillLayoutManager: FlexboxLayoutManager

    lateinit var jobId: String

    @Inject
    lateinit var presenter: EditJobPresenter

    @Inject
    lateinit var tagsCollectionPresenter: TagsCollectionPresenter

    private var jobDetailsSent: JobDetailsSent = JobDetailsSent()
    private var jobDetails: JobDetails = JobDetails()


    private var categoriesData: MutableList<JobCategory> = mutableListOf()
    private var subCategoriesData: MutableList<SubCategory> = mutableListOf()


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_edit_job)

        val component = DaggerEditJobComponent.builder()
            .editJobModule(EditJobModule(this))
            .tagsCollectionModule(TagsCollectionModule(this))
            .build()
        component.inject(this)

        jobId = intent.getStringExtra(jobId_key)

        init()

        presenter.attachView(this)

        tagsCollectionPresenter.attachView(this)
        tagsCollectionPresenter.getJobCategories(true)
        presenter.getJobDetails(jobId)


    }


    fun init() {
        skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        skillsAdapter = SkillsAdapter(this, mutableListOf(), false)
        skillsMain.layoutManager = skillLayoutManager
        skillsMain.adapter = skillsAdapter

        skillsDialog =
            AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.skills_edit_dialog, null))
                .create()



        educationLevel.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                EnumsProvider.educationLevels.map { it.name })
        jobType.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, EnumsProvider.jobTypes.map { it.name })


        jobDetailsSent.jobType = EnumsProvider.jobTypes.map { it.name }[0]
        jobDetailsSent.minimumEducationLevel = EnumsProvider.educationLevels.map { it.name }[0]

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
                if (jobId.isNotEmpty())
                    presenter.editJob(jobDetailsSent, jobId)
                else
                    Toast.makeText(this, "Business Id is not provided", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun submitData() {
        with(jobDetailsSent) {
            nameAr = nameArInput.text.toString()
            nameEn = nameEnInput.text.toString()
            qualificationsAr = qualificationsArInput.text.toString()
            qualificationsEn = qualificationsEnInput.text.toString()
            responsibilitiesAr = responsibilitiesArInput.text.toString()
            responsibilitiesEn = responsibilitiesEnInput.text.toString()
            descriptionAr = descriptionArInput.text.toString()
            descriptionEn = descriptionEnInput.text.toString()
            rangeSalary = salaryInput.text.toString()
        }

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            parent?.id == category.id -> {
                subCategoriesData = categoriesData[position].subCategories
                jobDetailsSent.category = categoriesData[position]
                jobDetailsSent.categoryId = categoriesData[position].id
            }
            parent?.id == subCategory.id -> {
                jobDetailsSent.subCategory = subCategoriesData[position]
                jobDetailsSent.subCategoryId = subCategoriesData[position].id
            }
            parent?.id == educationLevel.id -> {
                jobDetailsSent.minimumEducationLevel = EnumsProvider.educationLevels[position].name

            }
            parent?.id == jobType.id -> {
                jobDetailsSent.jobType = EnumsProvider.jobTypes[position].name

            }
        }
    }

    fun openSkillEditDialog() {

        skillsDialog.show()

        with(skillsDialog) {
            val tempSkills = mutableListOf<Tag>()
            skillsData?.let { for (tag in it) tempSkills.add(tag) }

            dialogSkillsAdapter = SkillsAdapter(this@EditJobActivity, tempSkills, true)

            var dialogSkillLayoutManager = FlexboxLayoutManager(this@EditJobActivity)
            dialogSkillLayoutManager.alignItems = AlignItems.BASELINE
            dialogSkillLayoutManager.flexWrap = FlexWrap.WRAP

            skills.layoutManager = dialogSkillLayoutManager
            skills.adapter = dialogSkillsAdapter

            var addedSkill: Tag? = null

            skillsAutoCompleteAdapter =
                SkillsAutoCompleteAdapter(
                    this@EditJobActivity,
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

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })

            addSkillBtn.setOnClickListener {
                if (addedSkill == null) {
                    val searchText = skillsSearchInput.text.toString()
                    if (searchText.isNotEmpty()) {
                        val index = skillsAutoCompleteAdapter.data.map { it.name }.indexOf(searchText)
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
                jobDetailsSent.tags = tempSkills.map { it.id ?: "" }.toMutableList()
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

    override fun onJobEdited(
        job: JobDetails,
        flag: Boolean,
        msg: String?
    ) {
        if (flag) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            onBackPressed()
        } else
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


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
        categoriesData = categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList()
        category.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categoriesData.map { it.getTitle() }
        )

        jobDetailsSent.category = categoriesData[0]
        jobDetailsSent.categoryId = categoriesData[0].id


        subCategoriesData = categoriesData[0].subCategories
        subCategory.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            subCategoriesData.map { it.getTitle() }
        )

        jobDetailsSent.subCategory = subCategoriesData[0]
        jobDetailsSent.subCategoryId = subCategoriesData[0].id

    }


    override fun onJobDetailsLoaded(jobDetails: JobDetails) {
        this.jobDetails = jobDetails
        bindView()
    }


    fun bindView() {

        nameArInput.setText(jobDetails.nameAr)
        nameEnInput.setText(jobDetails.nameAr)
        qualificationsArInput.setText(jobDetails.qualificationsAr)
        qualificationsEnInput.setText(jobDetails.qualificationsEn)
        responsibilitiesArInput.setText(jobDetails.responsibilitiesAr)
        responsibilitiesEnInput.setText(jobDetails.responsibilitiesEn)
        salaryInput.setText(jobDetails.rangeSalary)
        descriptionArInput.setText(jobDetails.descriptionAr)
        descriptionEnInput.setText(jobDetails.descriptionEn)


        category.setSelection(categoriesData.indexOf(jobDetails.category))
        subCategory.setSelection(subCategoriesData.indexOf(jobDetails.subCategory))
        educationLevel.setSelection(EnumsProvider.educationLevels.map { it.name }.indexOf(jobDetails.minimumEducationLevel))
        jobType.setSelection(EnumsProvider.jobTypes.map { it.name }.indexOf(jobDetails.jobType))
        jobDetailsSent.tags = jobDetails.tags?.map { it.name ?: "" }?.toMutableList()
        skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        skillsAdapter = SkillsAdapter(this, jobDetails.tags ?: mutableListOf(), false)
        skillsMain.layoutManager = skillLayoutManager
        skillsMain.adapter = skillsAdapter

    }

}
