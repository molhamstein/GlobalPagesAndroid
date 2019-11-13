package com.almersal.android.utilities

import com.almersal.android.data.entities.ApplicantStatus
import com.almersal.android.enums.ApplicantStatusEnum
import com.almersal.android.enums.EducationLevel
import com.almersal.android.enums.JobTypes
import com.firebase.jobdispatcher.Job

object EnumsProvider {
    var jobTypes: List<JobTypes> =
        listOf(JobTypes.fullTime, JobTypes.internship, JobTypes.partTime, JobTypes.projectBased, JobTypes.volunteer)
    var educationLevels: List<EducationLevel> = listOf(
        EducationLevel.associateDegree, EducationLevel.doctoralDegree, EducationLevel.highSchoolDegree,
        EducationLevel.masterDegree, EducationLevel.universityDegree

    )


    var applicantStatuses: List<ApplicantStatusEnum> = listOf(
        ApplicantStatusEnum.pending,
        ApplicantStatusEnum.interviewing,
        ApplicantStatusEnum.noHire,
        ApplicantStatusEnum.hire
    )

}