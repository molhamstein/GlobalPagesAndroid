package com.almersal.android.data.entities

import com.almersal.android.App

data class Job(
    val business: JobBusiness,
    val businessId: String,
    val category: JobCategory,
    val categoryId: String,
    val creationDate: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val id: String,
    val jobType: String,
    val minimumEducationLevel: String,
    val nameAr: String,
    val nameEn: String,
    val ownerId: String,
    val qualificationsAr: String,
    val qualificationsEn: String,
    val rangeSalary: String,
    val responsibilitiesAr: String,
    val responsibilitiesEn: String,
    val status: String,
    val subCategory: SubCategory,
    val subCategoryId: String
) {
    val name: String
        get() {
            return if (App.app.isArabic() && !nameAr.isBlank()) nameAr else nameEn
        }

    val description: String
        get() {
            return if (App.app.isArabic() && !descriptionAr.isBlank()) descriptionAr else descriptionEn
        }


    val qualifications: String
        get() {
            return if (App.app.isArabic() && !qualificationsAr.isBlank()) qualificationsAr else qualificationsEn
        }


    val responsibilities: String
        get() {
            return if (App.app.isArabic() && !responsibilitiesAr.isBlank())  responsibilitiesAr else responsibilitiesEn
        }


}