package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide

/**
 * Created by Adhamkh on 2018-07-18.
 */
class BusinessGuideViewHolder : RecyclerView.ViewHolder {

//    @BindView(R.id.businessCard)
//    lateinit var businessCard: CardView
//
//    @BindView(R.id.businessDetails)
//    lateinit var businessDetails: TextView
//
//    @BindView(R.id.businessTitle)
//    lateinit var businessTitle: TextView
//
//    @BindView(R.id.businessIcon)
//    lateinit var businessIcon: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(businessGuide: BusinessGuide) {
//        businessCard.setCardBackgroundColor(businessGuideEntity.color)
//        businessTitle.setText(businessGuideEntity.title)
//        businessDetails.setText(businessGuideEntity.details)
//        businessIcon.setImageResource(businessGuideEntity.icon)
    }

}