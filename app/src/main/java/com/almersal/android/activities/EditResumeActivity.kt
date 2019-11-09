package com.almersal.android.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.almersal.android.R
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_edit_resume.*
import kotlinx.android.synthetic.main.activity_edit_resume.education
import kotlinx.android.synthetic.main.activity_edit_resume.experiences
import kotlinx.android.synthetic.main.activity_edit_resume.profile_image
import kotlinx.android.synthetic.main.activity_edit_resume.references
import kotlinx.android.synthetic.main.activity_edit_resume.skills

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerEditResumeComponent
import com.almersal.android.di.module.EditUserResumeModule
import com.almersal.android.di.ui.EditResumeContract
import com.almersal.android.di.ui.EditResumePresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.listeners.ResumeDialogListener
import com.almersal.android.utilities.DateHelper.openDatePicker
import com.almersal.android.utilities.ValidationUtil.validate
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import kotlinx.android.synthetic.main.activity_edit_resume.adCities
import kotlinx.android.synthetic.main.activity_edit_resume.cityStateLayout
import kotlinx.android.synthetic.main.education_edit_dialog.*

import kotlinx.android.synthetic.main.experience_edit_dialog.*
import kotlinx.android.synthetic.main.experience_edit_dialog.doneExperience
import kotlinx.android.synthetic.main.experience_edit_dialog.descriptionExperience
import kotlinx.android.synthetic.main.skills_edit_dialog.*
import javax.inject.Inject

import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import java.io.File


class EditResumeActivity : BaseActivity(),
    EditResumeContract.View,
    View.OnClickListener,
    OnCitySelectListener, ResumeDialogListener {


    lateinit var experienceLayoutManager: LinearLayoutManager
    lateinit var skillLayoutManager: FlexboxLayoutManager
    lateinit var educationLayoutManager: LinearLayoutManager
    lateinit var referenceLayoutManager: LinearLayoutManager


    lateinit var experiencesAdapter: ExperiencesAdapter
    lateinit var referencesAdapter: ReferencesAdapter
    lateinit var skillsAdapter: SkillsAdapter
    lateinit var educationAdapter: EducationAdapter

    @Inject
    lateinit var presenter: EditResumePresenter

    private lateinit var progressDialog: ProgressDialog

    private var updatedProfile = SentUpdateProfile()

    var cityId: String? = null
    var user: User? = null

    lateinit var skillsDialog: AlertDialog
    lateinit var experienceDialog: AlertDialog
    lateinit var educationDialog: AlertDialog
    lateinit var skillsAutoCompleteAdapter: SkillsAutoCompleteAdapter
    lateinit var dialogSkillsAdapter: SkillsAdapter

    var PICTURE_REQUEST = 100

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_edit_resume)

        experienceLayoutManager = LinearLayoutManager(this)
        skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        educationLayoutManager = LinearLayoutManager(this)
        referenceLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        user = UserRepository(this).getUser()
        val component = DaggerEditResumeComponent.builder()
            .editUserResumeModule(EditUserResumeModule(this))
            .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.loadCities(withCache = true)
        progressDialog = ProgressDialog.newInstance()

        initDialogs()
        editExperienceBtn.setOnClickListener(this)
        editEductionBtn.setOnClickListener(this)
        editSkillsBtn.setOnClickListener(this)
        changeProfilePicBtn.setOnClickListener(this)
        doneBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)

    }


    private fun initDialogs() {
        skillsDialog =
            AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.skills_edit_dialog, null))
                .create()

        experienceDialog =
            AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.experience_edit_dialog, null))
                .create()


        educationDialog =
            AlertDialog.Builder(this).setView(layoutInflater.inflate(R.layout.education_edit_dialog, null))
                .create()
    }

    //____________________________________________________________________________________________
    //If both position and the object is null, then we are adding new object, else we are editing.
    override fun openExperienceEditDialog(position: Int?, experience: Experience?) {

        experienceDialog.show()
        with(experienceDialog) {

            if (experience != null) {
                companyName.setText(experience.companyName)
                descriptionExperience.setText(experience.description)
                fromExperience.setText(experience.from)
                positionTitle.setText(experience.title)
                toExperience.setText(experience.to)
                presentCheck.isChecked = experience.isPresent
            }
            fromExperience.setOnClickListener {
                openDatePicker(fromExperience, this@EditResumeActivity)
            }


            toExperience.setOnClickListener {
                openDatePicker(toExperience, this@EditResumeActivity)
            }

            doneExperience.setOnClickListener {
                if (validate(
                        this@EditResumeActivity,
                        companyName,
                        positionTitle,
                        fromExperience,
                        if (!presentCheck.isChecked) toExperience else fromExperience
                    )
                ) {
                    val addedExperience = Experience(
                        companyName.text.toString(),
                        descriptionExperience.text.toString(),
                        fromExperience.text.toString(),
                        positionTitle.text.toString(),
                        toExperience.text.toString(),
                        presentCheck.isChecked
                    )
                    if (experience == null)
                        experiencesAdapter.add(addedExperience)
                    else
                        experiencesAdapter.update(position!!, addedExperience)


                    updatedProfile.experience = experiencesAdapter.data
                    dismiss()
                }

            }

            presentCheck.setOnCheckedChangeListener { _, checked ->
                if (checked)
                    toExperience.visibility = View.GONE
                else
                    toExperience.visibility = View.VISIBLE

            }

        }

    }

    override fun openEducationEditDialog(position: Int?, education: Education?) {

        educationDialog.show()
        with(educationDialog) {

            if (education != null) {
                educationEntity.setText(education.educationalEntity)
                descriptionEducation.setText(education.description)
                educationTitle.setText(education.title)
                fromEducation.setText(education.from)
                toEducation.setText(education.to)
            }
            fromEducation.setOnClickListener {

                openDatePicker(fromEducation, this@EditResumeActivity)
            }

            toEducation.setOnClickListener {

                openDatePicker(toEducation, this@EditResumeActivity)

            }

            doneEducation.setOnClickListener {
                if (validate(
                        this@EditResumeActivity,
                        educationEntity,
                        educationTitle,
                        fromEducation,
                        toEducation
                    )
                ) {
                    val addedEducation = Education(
                        descriptionEducation.text.toString(),
                        educationEntity.text.toString(),
                        fromEducation.text.toString(),
                        educationTitle.text.toString(),
                        toEducation.text.toString()
                    )
                    if (education == null)
                        educationAdapter.add(addedEducation)
                    else
                        educationAdapter.update(position!!, addedEducation)

                    updatedProfile.education = educationAdapter.data
                    dismiss()
                }

            }


        }

    }
    //If both position and the object is null, then we are adding new object, else we are editing.
    //____________________________________________________________________________________________


    override fun openSkillEditDialog() {

        skillsDialog.show()

        with(skillsDialog) {
            val tempSkills = mutableListOf<Tag>()
            updatedProfile.skills?.let { for (tag in it) tempSkills.add(tag) }

            dialogSkillsAdapter = SkillsAdapter(this@EditResumeActivity, tempSkills, true)

            var dialogSkillLayoutManager = FlexboxLayoutManager(this@EditResumeActivity)
            dialogSkillLayoutManager.alignItems = AlignItems.BASELINE
            dialogSkillLayoutManager.flexWrap = FlexWrap.WRAP

            skills.layoutManager = dialogSkillLayoutManager
            skills.adapter = dialogSkillsAdapter

            var addedSkill: Tag? = null

            skillsAutoCompleteAdapter =
                SkillsAutoCompleteAdapter(this@EditResumeActivity, android.R.layout.simple_dropdown_item_1line)
            skillsSearchInput.threshold = 1
            skillsSearchInput.setAdapter(skillsAutoCompleteAdapter)
            skillsSearchInput.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    addedSkill = skillsAutoCompleteAdapter.getTagAt(position)

                }

            skillsSearchInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    Handler().postDelayed({ presenter.getTags(s.toString()) }, 500)
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
                updatedProfile.skills = tempSkills
                updatedProfile.tags = tempSkills.map { it.id!! }.toMutableList()
                skillsAdapter.data = tempSkills
                skillsAdapter.notifyDataSetChanged()
                dismiss()
            }

        }
    }

    override fun onClick(v: View?) {

        when (v) {
            editExperienceBtn -> {
                openExperienceEditDialog()
            }
            editEductionBtn -> {
                openEducationEditDialog()
            }
            editSkillsBtn -> {
                openSkillEditDialog()
            }
            changeProfilePicBtn -> {
                Pix.start(this, PICTURE_REQUEST, 1)
            }
            doneBtn -> {
                if (validate(this, nameInput, specsInput, phoneInput, bioInput)) {
                    updatedProfile.phoneNumber = phoneInput.text.toString()
                    updatedProfile.username = nameInput.text.toString()
                    updatedProfile.primaryIdentifier = specsInput.text.toString()
                    updatedProfile.bio = bioInput.text.toString()
                    presenter.updateProfile(updatedProfile)
                }
            }
            backBtn -> {
                onBackPressed()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    presenter.uploadProfilePic(File(returnValue[0]))
                    Log.v("", "")
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, ProductAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.Approve_Permissions_To_Pick_Images),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    override fun bindView() {


        phoneInput.setText(user?.phoneNumber)
        bioInput.setText(user?.CV?.bio)
        nameInput.setText(user?.username)
        specsInput.setText(user?.CV?.primaryIdentifier)
        BindingUtils.loadProfileImage(profile_image, user?.imageProfile)
        experiencesAdapter = ExperiencesAdapter(this, user?.CV?.experience ?: mutableListOf(), true, this)

        referencesAdapter = ReferencesAdapter(this, user?.CV?.let {
            with(it) {
                listOfNotNull(
                    behanceLink,
                    facebookLink,
                    githubLink,
                    twitterLink,
                    websiteLink
                )
            }
        } ?: listOf(), true)

        skillsAdapter = SkillsAdapter(this, user?.CV?.tags ?: mutableListOf(), false)
        educationAdapter = EducationAdapter(this, user?.CV?.education ?: mutableListOf(), true, this)
        with(updatedProfile) {
            imageProfile = user?.imageProfile ?: ""
            experience = user?.CV?.experience
            education = user?.CV?.education
            skills = user?.CV?.tags
            tags = skills?.map { it.id!! }?.toMutableList()
            behanceLink = user?.CV?.behanceLink
            facebookLink = user?.CV?.facebookLink
            githubLink = user?.CV?.githubLink
            twitterLink = user?.CV?.twitterLink
            websiteLink = user?.CV?.websiteLink
        }
        experiences.layoutManager = experienceLayoutManager
        references.layoutManager = referenceLayoutManager
        education.layoutManager = educationLayoutManager
        skills.layoutManager = skillLayoutManager

        experiences.adapter = experiencesAdapter
        references.adapter = referencesAdapter
        education.adapter = educationAdapter
        skills.adapter = skillsAdapter
    }


// ________________View Listeners________________

    override fun showCitiesProgress(show: Boolean) {
        if (show) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesEmptyView(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onCitiesLoaded(citiesList: MutableList<City>) {
        val citiesAdapter =
            CityRecyclerViewAdapter(context = baseContext, citiesListList = citiesList, onCitySelectListener = this)
        adCities.adapter = citiesAdapter
        user?.let { user ->
            user.CV?.let { CV ->
                CV.city?.let { city ->
                    onSelectCity(city)
                    citiesAdapter.setCheck(city)
                }
            }
        }


    }

    override fun showAutoCompleteProgress(flag: Boolean) {
        skillsDialog.addSkillBtn.visibility = if (flag) View.GONE else View.VISIBLE
        skillsDialog.progress.visibility = if (flag) View.VISIBLE else View.GONE


    }

    override fun onTagsLoaded(tags: List<Tag>) {
        skillsAutoCompleteAdapter.data = tags.toMutableList()
        skillsAutoCompleteAdapter.notifyDataSetChanged()

    }

    override fun onSelectCity(city: City) {
        updatedProfile.cityId = city.id
    }


    override fun onTagAdded(tag: Tag) {
        dialogSkillsAdapter.addItem(tag)
    }

    override fun onProfilePicLoaded(attachmentResponse: AttachmentResponse) {
        BindingUtils.loadProfileImage(profile_image, attachmentResponse.thumbnail)
        updatedProfile.imageProfile = attachmentResponse.url

    }

    override fun onProfileUpdated() {
        onBackPressed()
    }

    override fun onCvUploaded(attachmentResponse: AttachmentResponse) {

    }

    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }


}
