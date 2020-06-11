package com.almersal.android.viewModel

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.MediaViewPagerAdapter
import com.almersal.android.data.entities.Product
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.utilities.BindingUtils
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.product_main_fixed_layout.*
import org.jetbrains.annotations.Nullable

class ProductViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
    var context: Context

    @Nullable
    @BindView(R.id.mediaViewPager)
    lateinit var mediaViewPager: ViewPager

    @BindView(R.id.postCategory)
    lateinit var postCategory: TextView

    @BindView(R.id.postSubCategory)
    lateinit var postSubCategory: TextView

    @BindView(R.id.postCreatedDate)
    lateinit var postCreatedDate: TextView

    @BindView(R.id.postTitle)
    lateinit var postTitle: TextView

    @BindView(R.id.postCity)
    lateinit var postCity: TextView

    @BindView(R.id.postLocation)
    lateinit var postLocation: TextView

    @BindView(R.id.postInLocation)
    lateinit var postInLocation: TextView

    @BindView(R.id.postDescription)
    lateinit var postDescription: TextView

    @BindView(R.id.viewPagerIndicator)
    lateinit var viewPagerIndicator: CircleIndicator

    @BindView(R.id.editProduct)
    lateinit var editProduct: ImageView

    @BindView(R.id.price)
    lateinit var price: TextView

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }


    fun bind(product: Product) {


        mediaViewPager?.adapter = MediaViewPagerAdapter(context, product.getMedias())
        price.text = (product.price ?: 0).toString()
        viewPagerIndicator.setViewPager(mediaViewPager)

        postCategory.text = product.category.getTitle()
        postSubCategory.text = product.subCategory.getTitle()

        postTitle.text = product.title
        postCity.text = product.city.getTitle()
        postLocation.text = product.location.getTitle()

        val inLocation =
            context.resources.getString(R.string.In) + " " + product.location.getTitle()
        postInLocation.text = inLocation

        postDescription.text = product.description
        postDescription.movementMethod = LinkMovementMethod.getInstance()
        postCreatedDate.text = DateNormalizer.getCanonicalDateTime(product.creationDate)
    }
}
