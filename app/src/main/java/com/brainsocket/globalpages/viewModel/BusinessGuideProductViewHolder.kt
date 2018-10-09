package com.brainsocket.globalpages.viewModel

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.ProductThumb
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

    init {
        ButterKnife.bind(this, view)
    }

    fun bind(productThumb: ProductThumb) {
        BindingUtils.loadProductImage(productImage, productThumb)
        productTitle.setText(productThumb.name)
        productDescription.setText(productThumb.description)
    }


}