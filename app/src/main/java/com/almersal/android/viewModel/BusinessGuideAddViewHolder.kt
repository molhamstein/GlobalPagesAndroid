package com.almersal.android.viewModel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.App
import com.almersal.android.R
import com.almersal.android.adapters.AttachmentRecyclerViewAdapter
import com.almersal.android.adapters.CategoryRecyclerViewAdapter
import com.almersal.android.adapters.SubCategoryRecyclerViewAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesModel.BusinessGuideEditModel
import com.almersal.android.data.entitiesModel.BusinessGuideModel
import com.almersal.android.data.validations.ValidationHelper
import com.almersal.android.repositories.UserRepository


class BusinessGuideAddViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.businessId)
    lateinit var businessId: TextView

    @BindView(R.id.businessName)
    lateinit var businessName: EditText

    @BindView(R.id.businessImages)
    lateinit var businessImages: RecyclerView

    @BindView(R.id.phoneNumber)
    lateinit var phoneNumber: EditText

    @BindView(R.id.phoneNumber2)
    lateinit var phoneNumber2: EditText

    @BindView(R.id.businessTypes)
    lateinit var businessTypes: RecyclerView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: RecyclerView

    @BindView(R.id.locationEditText)
    lateinit var locationEditText: EditText

    @BindView(R.id.description)
    lateinit var description: EditText

    @BindView(R.id.fax)
    lateinit var fax: EditText

    var context: Context

    var openDayList: MutableList<String> = mutableListOf()

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun isValid(): Boolean {
        val businessGuideModel = getBusinessGuideModel()
        if (ValidationHelper.isEmpty(businessGuideModel.nameAr)) {
            businessName.error = context.resources.getString(R.string.enteremail)
            businessName.requestFocus()
            return false
        }
//        if (!ValidationHelper.isEmail(loginModel.email)) {
//            email.error = context.resources.getString(R.string.entercorrectemail)
//            email.requestFocus()
//            return false
//        }

        return true
    }

    fun isAdd(): Boolean = businessId.text.isNullOrEmpty()

    fun getBusinessGuideModel(): BusinessGuideModel {
        val businessGuideModel = if (businessId.text.isNullOrEmpty()) BusinessGuideModel() else BusinessGuideEditModel()

        if (businessGuideModel is BusinessGuideEditModel)
            businessGuideModel.id = businessId.text.toString()

        businessGuideModel.nameAr = businessName.text.toString()
        businessGuideModel.nameEn = businessName.text.toString()

        for (item in (businessImages.adapter as AttachmentRecyclerViewAdapter).attachmentList) {
            businessGuideModel.covers.add(MediaEntity(item.name))
        }

        businessGuideModel.phone1 = phoneNumber.text.toString()
        businessGuideModel.phone2 = phoneNumber2.text.toString()

        val cat = (businessTypes.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
        if (cat != null)
            businessGuideModel.categoryId = cat.id

        if (businessSubCategories.adapter != null) {
            val subCat: SubCategory? = (businessSubCategories.adapter as SubCategoryRecyclerViewAdapter).getCurrentSubCategory()
            if (subCat != null)
                businessGuideModel.subCategoryId = subCat.id
        }

        if (locationEditText.text.isNotEmpty()) {
            val point = locationEditText.text.toString().split(";")
            businessGuideModel.locationPoint = PointEntity(point[0].toDouble(), point[1].toDouble())
        } else {
            businessGuideModel.locationPoint = PointEntity()
        }

        businessGuideModel.description = description.text.toString()
        businessGuideModel.fax = fax.text.toString()
        businessGuideModel.openingDays = openDayList
        if (businessGuideModel.openingDays.size > 0)
            businessGuideModel.openingDaysEnabled = true
        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
        return businessGuideModel
    }

    fun bindBusinessGuide(businessGuide: BusinessGuide) {
        businessId.text = businessGuide.id

        businessName.setText(businessGuide.nameEn)

        businessImages.adapter = AttachmentRecyclerViewAdapter(context, businessGuide.covers.map { Attachment(it.url) }.toMutableList())

        phoneNumber.setText(businessGuide.phone1)
        phoneNumber2.setText(businessGuide.phone2)

        if (businessTypes.adapter != null) {
            (businessTypes.adapter as CategoryRecyclerViewAdapter).setCheck(businessGuide.category)
            val cat = (businessTypes.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(context, cat.subCategoriesList)
        }

        if (businessSubCategories.adapter != null) {
            (businessSubCategories.adapter as SubCategoryRecyclerViewAdapter).setCheck(businessGuide.subCategory)
        }

        val resultPoint = businessGuide.locationPoint.lat.toString() + ";" + businessGuide.locationPoint.lng.toString()
        locationEditText.setText(resultPoint)


        description.setText(businessGuide.description)
        fax.setText(businessGuide.fax)
        openDayList = businessGuide.openingDays
        if (businessGuide.openingDays.size > 0)
            businessGuide.openingDaysEnabled = true
//        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
    }


}