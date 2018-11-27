package com.almersal.android.viewHolders

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-07-18.
 */
class BusinessGuideViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.businessCard)
    lateinit var businessCard: CardView
    //
    @BindView(R.id.businessDetails)
    lateinit var businessDetails: TextView

    @BindView(R.id.businessTitle)
    lateinit var businessTitle: TextView

    @BindView(R.id.businessCategory)
    lateinit var businessCategory: TextView

    @BindView(R.id.businessIcon)
    lateinit var businessIcon: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(businessGuide: BusinessGuide) {
        businessTitle.text = businessGuide.getName()
        businessDetails.text = businessGuide.description
        businessCategory.text = businessGuide.category.getTitle()
        BindingUtils.loadBusinessGuideImage2(businessIcon, businessGuide)
    }

}