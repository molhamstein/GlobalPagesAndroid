package com.brainsocket.globalpages.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuideProductModule
import com.brainsocket.globalpages.di.ui.AttachmentContract
import com.brainsocket.globalpages.di.ui.AttachmentPresenter
import com.brainsocket.globalpages.di.ui.BusinessGuideProductContract
import com.brainsocket.globalpages.di.ui.BusinessGuideProductPresenter
import com.brainsocket.globalpages.viewModel.BusinessGuideProductViewHolder
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject


class ProductDetailsActivity : BaseActivity() {

    companion object {
        const val ProductManageActivity_Tag = "ProductManageActivity_Tag"
    }


    lateinit var productThumb: ProductThumb

    private lateinit var businessGuideProductViewHolder: BusinessGuideProductViewHolder


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.product_manage_layout)
        ButterKnife.bind(this)
        val jSon = intent.getStringExtra(ProductManageActivity_Tag)
        productThumb = Gson().fromJson(jSon, ProductThumb::class.java)
        businessGuideProductViewHolder = BusinessGuideProductViewHolder(findViewById(android.R.id.content))
        businessGuideProductViewHolder.bind(productThumb)

    }


    @OnClick(R.id.productCloseBtn)
    fun onProductCloseButtonClick(view: View) {
        finish()
    }


}