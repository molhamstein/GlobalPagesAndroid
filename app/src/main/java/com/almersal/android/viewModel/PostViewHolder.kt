package com.almersal.android.viewModel

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.MediaViewPagerAdapter
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.normalization.DateNormalizer
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator

class PostViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    var context: Context

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

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun bind(post: Post) {

        mediaViewPager.adapter = MediaViewPagerAdapter(context, post.media)
        viewPagerIndicator.setViewPager(mediaViewPager)

        postCategory.text = post.category.getTitle()
        postSubCategory.text = post.subCategory.getTitle()

        postTitle.text = post.title
        postCity.text = post.city.getTitle()
        postLocation.text = post.location.getTitle()

        val inLocation = context.resources.getString(R.string.In) + " " + post.location.getTitle()
        postInLocation.text = inLocation

        postDescription.text = post.description
        postDescription.movementMethod = LinkMovementMethod.getInstance()

        postCreatedDate.text = DateNormalizer.getCanonicalDateTime(post.creationDate)
    }



}