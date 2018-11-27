package com.almersal.android.viewModel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.data.entitiesModel.ProductThumbEditModel
import com.almersal.android.data.entitiesModel.ProductThumbModel
import com.almersal.android.data.validations.ValidationHelper
import com.almersal.android.utilities.BindingUtils

class BusinessGuideProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.productId)
    lateinit var productId: TextView

    @BindView(R.id.productImageTag)
    lateinit var productImageTag: TextView

    @BindView(R.id.productImage)
    lateinit var productImage: ImageView

    @BindView(R.id.productTitle)
    lateinit var productTitle: EditText

    @BindView(R.id.productDescription)
    lateinit var productDescription: EditText

    var context: Context

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun isValid(): Boolean {
        val productThumbModel: ProductThumbModel = getProductThumbModel()
        if (ValidationHelper.isEmpty(productThumbModel.name)) {
            productTitle.error = context.resources.getString(R.string.enteremail)
            productTitle.requestFocus()
            return false
        }
        return true
    }

    fun isAdd(): Boolean = productId.text.isNullOrEmpty()

    fun getProductThumbModel(): ProductThumbModel {

        val productThumbModel = if (productId.text.isNullOrEmpty()) ProductThumbModel() else ProductThumbEditModel()

        if (productThumbModel is ProductThumbEditModel)
            productThumbModel.id = productId.text.toString()

        productThumbModel.name = productTitle.text.toString()
        productThumbModel.description = productDescription.text.toString()
        productThumbModel.image = productImageTag.text.toString() // if (productImage.tag != null) productImage.tag.toString() else ""

        return productThumbModel
    }

    fun bind(productThumb: ProductThumb) {
        productId.text = productThumb.id

        BindingUtils.loadProductImage(productImage, productThumb)
        productImageTag.text = productThumb.image
        productTitle.setText(productThumb.name)
        productDescription.setText(productThumb.description)
    }


}