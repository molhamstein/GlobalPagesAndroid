package com.brainsocket.globalpages.viewModel

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.data.entitiesModel.ProductThumbModel
import com.brainsocket.globalpages.data.validations.ValidationHelper
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

    fun getProductThumbModel(): ProductThumbModel {
        val productThumbModel = ProductThumbModel()
        productThumbModel.name = productTitle.text.toString()
        productThumbModel.description = productDescription.text.toString()
        productThumbModel.image = if (productImage.tag != null) productImage.tag.toString() else ""

        return productThumbModel
    }

    fun bind(productThumb: ProductThumb) {
        BindingUtils.loadProductImage(productImage, productThumb)
        productImage.tag = productThumb.image
        productTitle.setText(productThumb.name)
        productDescription.setText(productThumb.description)
    }


}