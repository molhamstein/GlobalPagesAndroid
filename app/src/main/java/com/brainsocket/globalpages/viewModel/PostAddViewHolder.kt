package com.brainsocket.globalpages.viewModel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.App
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.*
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.data.entitiesModel.PostEditModel
import com.brainsocket.globalpages.data.entitiesModel.PostModel
import com.brainsocket.globalpages.data.validations.ValidationHelper
import com.brainsocket.globalpages.repositories.UserRepository

/**
 * Created by Adhamkh on 2018-10-09.
 */
class PostAddViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    var context: Context

    @BindView(R.id.postId)
    lateinit var postId: TextView

    @BindView(R.id.adTitle)
    lateinit var adTitle: EditText

    @BindView(R.id.adDescription)
    lateinit var adDescription: EditText

    @BindView(R.id.adImages)
    lateinit var adImages: RecyclerView

    @BindView(R.id.adCategories)
    lateinit var adCategories: RecyclerView

    @BindView(R.id.adSubCategories)
    lateinit var adSubCategories: RecyclerView

    @BindView(R.id.adCities)
    lateinit var adCities: RecyclerView

    @BindView(R.id.adLocations)
    lateinit var adLocations: RecyclerView


    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun isValid(): Boolean {
        val postModel = getPostModel()
        if (ValidationHelper.isEmpty(postModel.title)) {
            adTitle.error = context.resources.getString(R.string.enteremail)
            adTitle.requestFocus()
            return false
        }
//        if (!ValidationHelper.isEmail(loginModel.email)) {
//            email.error = context.resources.getString(R.string.entercorrectemail)
//            email.requestFocus()
//            return false
//        }

        return true
    }

    fun isAdd(): Boolean = postId.text.isNullOrEmpty()

    fun getPostModel(): PostModel {
        val postModel = if (postId.text.isNullOrEmpty()) PostModel() else PostEditModel()
        if (postModel is PostEditModel)
            postModel.id = postId.text.toString()

        postModel.title = adTitle.text.toString()
//        postModel.name = postModel.title
        postModel.description = adDescription.text.toString()

        for (item in (adImages.adapter as AttachmentRecyclerViewAdapter).attachmentList) {
            postModel.media.add(MediaEntity(item.name))
        }

        if (adCategories.adapter != null) {
            val cat = (adCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                postModel.categoryId = cat.id
        }

        if (adSubCategories.adapter != null) {
            val subCat: SubCategory? = (adSubCategories.adapter as SubCategoryRecyclerViewAdapter).getCurrentSubCategory()
            if (subCat != null)
                postModel.subCategoryId = subCat.id
        }

        if (adCities.adapter != null) {
            val city: City? = (adCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
            if (city != null)
                postModel.cityId = city.id
        }

        if (adLocations.adapter != null) {
            val location: LocationEntity? = (adLocations.adapter as LocationEntityRecyclerViewAdapter).getCurrentLocation()
            if (location != null)
                postModel.locationId = location.id
        }

        postModel.ownerId = UserRepository(App.app).getUser()!!.id!!
        return postModel
    }

    fun bindPost(post: Post) {
        postId.text = post.id


        adTitle.setText(post.title)
        adDescription.setText(post.description)

        adImages.adapter = AttachmentRecyclerViewAdapter(context, post.media.map { Attachment(it.url) }.toMutableList())


        if (adCategories.adapter != null) {
            (adCategories.adapter as CategoryRecyclerViewAdapter).setCheck(post.category)
            val cat = (adCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                adSubCategories.adapter = SubCategoryRecyclerViewAdapter(context, cat.subCategoriesList)
        }

        if (adSubCategories.adapter != null) {
            (adSubCategories.adapter as SubCategoryRecyclerViewAdapter).setCheck(post.subCategory)
        }


        if (adCities.adapter != null) {
            (adCities.adapter as CityRecyclerViewAdapter).setCheck(post.city)
            val city = (adCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
            if (city != null)
                adLocations.adapter = LocationEntityRecyclerViewAdapter(context, city.locations)
        }

        if (adLocations.adapter != null) {
            (adLocations.adapter as LocationEntityRecyclerViewAdapter).setCheck(post.location)
        }

//        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
    }


}