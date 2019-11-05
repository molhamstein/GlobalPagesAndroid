package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.almersal.android.R
import com.almersal.android.adapters.EducationAdapter
import com.almersal.android.adapters.ExperiencesAdapter
import com.almersal.android.adapters.ReferencesAdapter
import com.almersal.android.adapters.SkillsAdapter
import com.almersal.android.data.entities.User
import com.almersal.android.data.entitiesResponses.AttachmentResponse
import com.almersal.android.di.component.DaggerUserResumeComponent
import com.almersal.android.di.module.UserResumeModule
import com.almersal.android.di.ui.UserResumeContract
import com.almersal.android.di.ui.UserResumePresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_resume.*
import javax.inject.Inject

class UserResumeActivity : BaseActivity(), UserResumeContract.View, View.OnClickListener {


    lateinit var experienceLayoutManager: LinearLayoutManager
    lateinit var skillLayoutManager: FlexboxLayoutManager
    lateinit var educationLayoutManager: LinearLayoutManager
    lateinit var referenceLayoutManager: LinearLayoutManager


    lateinit var experiencesAdapter: ExperiencesAdapter
    lateinit var referencesAdapter: ReferencesAdapter
    lateinit var skillsAdapter: SkillsAdapter
    lateinit var educationAdapter: EducationAdapter

    @Inject
    lateinit var presenter: UserResumePresenter

    private lateinit var progressDialog: ProgressDialog

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_resume)

        progressDialog = ProgressDialog.newInstance()

        val component = DaggerUserResumeComponent.builder()
            .userResumeModule(UserResumeModule(this))
            .build()



        component.inject(this)
        presenter.attachView(this)


        experienceLayoutManager = LinearLayoutManager(this)
        skillLayoutManager = FlexboxLayoutManager(this)
        skillLayoutManager.alignItems = AlignItems.BASELINE
        skillLayoutManager.flexWrap = FlexWrap.WRAP
        educationLayoutManager = LinearLayoutManager(this)
        referenceLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        closeBtn.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()
        val user = UserRepository(this).getUser()!!
        presenter.getUserResume(user.id!!)

    }


    override fun updateUserInfo(user: User?) {
        BindingUtils.loadProfileImage(profile_image, user?.imageProfile)
        name.text = user?.username
        bioText.text = user?.CV?.bio
        locationText.text = user?.CV?.city?.getTitle()
        specs.text = user?.CV?.primaryIdentifier
        experiencesAdapter = ExperiencesAdapter(this, user?.CV?.experience ?: mutableListOf(), false)

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
        } ?: listOf(), false)

        skillsAdapter = SkillsAdapter(this, user?.CV?.tags ?: mutableListOf(), false)
        educationAdapter = EducationAdapter(this, user?.CV?.education ?: mutableListOf(), false)

        experiences.layoutManager = experienceLayoutManager
        references.layoutManager = referenceLayoutManager
        education.layoutManager = educationLayoutManager
        skills.layoutManager = skillLayoutManager

        experiences.adapter = experiencesAdapter
        references.adapter = referencesAdapter
        education.adapter = educationAdapter
        skills.adapter = skillsAdapter

    }




    override fun getFailed() {
        Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show()
    }


    override fun onClick(v: View?) {
        when (v) {
            closeBtn -> {
                onBackPressed()
            }
        }
    }


    override fun showProgress(show: Boolean) {
        if (show)
            progressDialog.show(supportFragmentManager, ProgressDialog.ProgressDialog_Tag)
        else
            progressDialog.dismiss()
    }
}
