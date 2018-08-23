package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-06-28.
 */
class BusinessGuideSliderViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.businessTitle)
    lateinit var businessTitle: TextView

    @BindView(R.id.businessIcon)
    lateinit var businessIcon: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(businessGuide: BusinessGuide) {
        businessTitle.setText(businessGuide.getName())
        BindingUtils.loadBusinessGuideImage(businessIcon, businessGuide)
    }

}