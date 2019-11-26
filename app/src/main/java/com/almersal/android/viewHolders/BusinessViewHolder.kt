package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.almersal.android.data.entities.BusinessGuide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.business_item_layout.*

class BusinessViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer{
    fun bind(businessGuide: BusinessGuide) {
        business_toggle.textOff = businessGuide.getName()
        business_toggle.textOn = businessGuide.getName()
        business_toggle.text = businessGuide.getName()
        business_toggle.setOnCheckedChangeListener(null)
        business_toggle.isChecked = businessGuide.isSelected
    }
}