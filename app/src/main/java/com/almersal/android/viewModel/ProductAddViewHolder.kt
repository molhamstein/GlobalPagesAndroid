package com.almersal.android.viewModel

import android.content.Context
import android.support.v7.widget.AppCompatEditText
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

class ProductAddViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    var context: Context

    @BindView(R.id.productId)
    lateinit var productId: TextView

    @BindView(R.id.productName)
    lateinit var productTitle: EditText

    @BindView(R.id.productDescription)
    lateinit var productDescription: EditText

    @BindView(R.id.productImages)
    lateinit var productImages: RecyclerView


    @BindView(R.id.productCategories)
    lateinit var productCategories: RecyclerView

    @BindView(R.id.productSubCategories)
    lateinit var productSubCategories: RecyclerView

    @BindView(R.id.productCities)
    lateinit var productCities: RecyclerView

    @BindView(R.id.productLocations)
    lateinit var productLocations: RecyclerView

    @BindView(R.id.price)
    lateinit var price: AppCompatEditText

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun isValid(): Boolean {
        val postModel = getProductModel(UserRepository(context).getUser()!!.id ?: "")
        if (ValidationHelper.isEmpty(postModel.title ?: "")) {
            productTitle.error = context.resources.getString(R.string.enteremail)
            productTitle.requestFocus()
            return false
        }
//        if (!ValidationHelper.isEmail(loginModel.email)) {
//            email.error = context.resources.getString(R.string.entercorrectemail)
//            email.requestFocus()
//            return false
//        }

        return true
    }

    fun isAdd(): Boolean = productId.text.isNullOrEmpty()

    fun getProductModel(userId: String, businessId: String? = null): ProductAddModel {
        val product = ProductAddModel()
        product.id = productId.text.toString()
        product.businessId = businessId

        var title = productTitle.text.toString()
        product.titleAr = title
        product.titleEn = title
//        postModel.name = postModel.title
        product.descriptionAr = productDescription.text.toString()
        product.descriptionEn = productDescription.text.toString()

        for (item in (productImages.adapter as AttachmentRecyclerViewAdapter).attachmentList) {
            product.media.add(item.thumbnail)
        }


        if (productCategories.adapter != null) {

            val cat = (productCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                product.categoryId = cat.id
        }

        if (productSubCategories.adapter != null) {
            val subCat: SubCategory? =
                (productSubCategories.adapter as SubCategoryRecyclerViewAdapter).getCurrentSubCategory()
            if (subCat != null)
                product.subCategoryId = subCat.id
        }

        if (productCities.adapter != null) {
            val city: City? = (productCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
            if (city != null)
                product.cityId = city.id
        }

        if (productLocations.adapter != null) {
            val location: LocationEntity? =
                (productLocations.adapter as LocationEntityRecyclerViewAdapter).getCurrentLocation()
            if (location != null)
                product.locationId = location.id
        }

        product.ownerId = UserRepository(App.app).getUser()!!.id!!
        return product
    }

    fun bindProduct(product: Product) {
        productId.text = product.id

        price.setText((product.price ?: null).toString())
        productTitle.setText(product.title)
        productDescription.setText(product.description)

        productImages.adapter = AttachmentRecyclerViewAdapter(context, product.media.map {
            Attachment(it)
        }.toMutableList())


        if (productCategories.adapter != null) {
            val pos = (productCategories.adapter as CategoryRecyclerViewAdapter).setCheck(product.category)
            if (pos > 0)
                productCategories.smoothScrollToPosition(pos)
            val cat = (productCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (cat != null)
                productSubCategories.adapter = SubCategoryRecyclerViewAdapter(context, cat.subCategoriesList)
        }

        if (productSubCategories.adapter != null) {
            val pos = (productSubCategories.adapter as SubCategoryRecyclerViewAdapter).setCheck(product.subCategory)
            if (pos > 0)
                productSubCategories.smoothScrollToPosition(pos)
        }


        if (productCities.adapter != null) {
            val pos = (productCities.adapter as CityRecyclerViewAdapter).setCheck(product.city)
            if (pos > 0)
                productCities.smoothScrollToPosition(pos)
            val city = (productCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
            if (city != null)
                productLocations.adapter = LocationEntityRecyclerViewAdapter(context, city.locations)
        }

        if (productLocations.adapter != null) {
            val pos = (productLocations.adapter as LocationEntityRecyclerViewAdapter).setCheck(product.location)
            if (pos > 0)
                productLocations.smoothScrollToPosition(pos)
        }

//        businessGuideModel.ownerId = UserRepository(App.app).getUser()!!.id!!
    }


}