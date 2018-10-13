package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.utilities.BindingUtils

class ProductViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    init {
        ButterKnife.bind(this, view)
    }


    @BindView(R.id.editProduct)
    lateinit var editProduct: ImageView

    @BindView(R.id.productImage)
    lateinit var productImage: ImageView

    @BindView(R.id.productTitle)
    lateinit var productTitle: TextView

    @BindView(R.id.productDetails)
    lateinit var productDetails: TextView


    fun bind(productThumb: ProductThumb, isOwner: Boolean) {
        BindingUtils.loadProductImage(productImage, productThumb)
        productTitle.text = productThumb.name
        productDetails.text = productThumb.description
        if (!isOwner) {
            editProduct.visibility = View.INVISIBLE
        } else {
            editProduct.visibility = View.VISIBLE
        }
    }

}