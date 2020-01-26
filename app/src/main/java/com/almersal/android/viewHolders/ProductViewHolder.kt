package com.almersal.android.viewHolders

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.MediaViewPagerAdapter
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.BindingUtils
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.product_main_fixed_layout.*
import org.jetbrains.annotations.Nullable



class ProductViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(product: Product) {
        product_Title.text = product.title
        product_details.text = product.description
        product_Tag.text = product.category.getTitle()
        product_city.text = product.city.getTitle()
        product_area.text = product.location.getTitle()

        BindingUtils.loadProductImage(product_Image, product)
    }
}