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
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesModel.PostEditModel
import com.almersal.android.data.entitiesModel.PostModel
import com.almersal.android.data.validations.ValidationHelper
import com.almersal.android.repositories.UserRepository

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

    @BindView(R.id.adVideos)
    lateinit var adVideos: RecyclerView

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

        for (item in (adVideos.adapter as VideoAttachmentRecyclerViewAdapter).attachmentList) {
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
            val pos = (adCategories.adapter as CategoryRecyclerViewAdapter).setCheck(post.category)
            if (pos > 0)
                adCategories.smoothScrollToPosition(pos)
            val cat = (adCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                adSubCategories.adapter = SubCategoryRecyclerViewAdapter(context, cat.subCategoriesList)
        }

        if (adSubCategories.adapter != null) {
            val pos = (adSubCategories.adapter as SubCategoryRecyclerViewAdapter).setCheck(post.subCategory)
            if (pos > 0)
                adSubCategories.smoothScrollToPosition(pos)
        }


        if (adCities.adapter != null) {
            val pos = (adCities.adapter as CityRecyclerViewAdapter).setCheck(post.city)
            if (pos > 0)
                adCities.smoothScrollToPosition(pos)
            val city = (adCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
            if (city != null)
                adLocations.adapter = LocationEntityRecyclerViewAdapter(context, city.locations)
        }

        if (adLocations.adapter != null) {
            val pos=(adLocations.adapter as LocationEntityRecyclerViewAdapter).setCheck(post.location)
            if(pos>0)
                adLocations.smoothScrollToPosition(pos)
        }

//        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
    }


}