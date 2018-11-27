package com.almersal.android.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.BusinessGuideProductModule
import com.almersal.android.di.ui.AttachmentContract
import com.almersal.android.di.ui.AttachmentPresenter
import com.almersal.android.di.ui.BusinessGuideProductContract
import com.almersal.android.di.ui.BusinessGuideProductPresenter
import com.almersal.android.viewModel.BusinessGuideProductViewHolder
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