package com.brainsocket.globalpages.viewModel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.App
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.AttachmentRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.LocationEntity
import com.brainsocket.globalpages.data.entities.MediaEntity
import com.brainsocket.globalpages.data.entities.PointEntity
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.data.entitiesModel.BusinessGuideModel
import com.brainsocket.globalpages.data.validations.ValidationHelper
import com.brainsocket.globalpages.repositories.UserRepository

/**
 * Created by Adhamkh on 2018-09-15.
 */
class BusinessGuideAddViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

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

    lateinit var context: Context

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

    fun getBusinessGuideModel(): BusinessGuideModel {
        val businessGuideModel = BusinessGuideModel()
        businessGuideModel.nameAr = businessName.text.toString()
        businessGuideModel.nameEn = businessName.text.toString()

        for (item in (businessImages.adapter as AttachmentRecyclerViewAdapter).attachmentList) {
            businessGuideModel.media.add(MediaEntity(item.name))
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

//        businessGuideModel.userId = UserRepository(App.app).getUser()!!.id!!
        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
        return businessGuideModel
    }


}