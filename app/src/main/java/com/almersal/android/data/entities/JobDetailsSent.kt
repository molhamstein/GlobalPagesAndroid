package com.almersal.android.data.entities

import com.almersal.android.App

class JobDetailsSent(
    var NumberOfApplicants: Int? = null,
    var business: JobBusiness? = null,
    var businessId: String? = null,
    var category: JobCategory? = null,
    var categoryId: String? = null,
    var creationDate: String? = null,
    var descriptionAr: String? = null,
    var descriptionEn: String? = null,
    var id: String? = null,
    var jobType: String? = null,
    var minimumEducationLevel: String? = null,
    var nameAr: String? = null,
    var nameEn: String? = null,
    var ownerId: String? = null,
    var qualificationsAr: String? = null,
    var qualificationsEn: String? = null,
    var rangeSalary: String? = null,
    var responsibilitiesAr: String? = null,
    var responsibilitiesEn: String? = null,
    var status: String? = null,
    var subCategory: SubCategory? = null,
    var subCategoryId: String? = null,
    var userIsApplied: Boolean = false,
    var tags: MutableList<String>? = null
) {
    val name: String?
        get() {
            return if (App.app.isArabic()) nameAr else nameEn
        }

    val description: String?
        get() {
            return if (App.app.isArabic()) descriptionAr else descriptionEn
        }


    val qualifications: String?
        get() {
            return if (App.app.isArabic()) qualificationsAr else qualificationsEn
        }


    val responsibilities: String?
        get() {
            return if (App.app.isArabic()) responsibilitiesAr else responsibilitiesEn
        }
}