package com.brainsocket.globalpages.viewModel

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideProductRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.MediaViewPagerAdapter
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.repositories.DataStoreRepositories
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.mediaViewPager)
    lateinit var mediaViewPager: ViewPager

    @BindView(R.id.viewPagerIndicator)
    lateinit var viewPagerIndicator: CircleIndicator

    @BindView(R.id.businessTitle)
    lateinit var businessTitle: TextView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: TextView

    @BindView(R.id.businessCategory)
    lateinit var businessCategory: TextView

    @BindView(R.id.businessSubCategories2)
    lateinit var businessSubCategories2: TextView

    @BindView(R.id.businessCity)
    lateinit var businessCity: TextView

    @BindView(R.id.businessLocation)
    lateinit var businessLocation: TextView

    @BindView(R.id.businessDescription)
    lateinit var businessDescription: TextView

    @BindView(R.id.businessGuideProductRecyclerView)
    lateinit var businessGuideProductRecyclerView: RecyclerView

    var context: Context

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun bind(businessGuide: BusinessGuide) {
        mediaViewPager.adapter = MediaViewPagerAdapter(context, businessGuide.covers/* DummyDataRepositories.getMediaList()*/)
        viewPagerIndicator.setViewPager(mediaViewPager)

        businessTitle.text = businessGuide.getName()

        businessCategory.text = businessGuide.category.getTitle()

        businessSubCategories.text = businessGuide.subCategory.getTitle()
        businessSubCategories2.text = businessGuide.subCategory.getTitle()

        businessCity.text = DataStoreRepositories(context).findCityById(businessGuide.cityId)?.getTitle()
        businessLocation.text = DataStoreRepositories(context).findLocationById(businessGuide.cityId,
                businessGuide.locationId)?.getTitle()

        businessDescription.text = businessGuide.description

        businessGuideProductRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        businessGuideProductRecyclerView.adapter = BusinessGuideProductRecyclerViewAdapter(context, businessGuide.products)

    }

}