package com.almersal.android.listeners

import com.almersal.android.data.entities.Education
import com.almersal.android.data.entities.Experience
import com.almersal.android.data.entities.Tag

interface ResumeDialogListener {
    fun openEducationEditDialog(position: Int? = null, education: Education? = null)
    fun openExperienceEditDialog(position: Int? = null, experience: Experience? = null)
    fun openSkillEditDialog()
}