package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.BusinessGuideViewHolder

class BusinessGuideRecyclerViewAdapter constructor(var context: Context, var businessGuideList: MutableList<BusinessGuide>)
    : RecyclerView.Adapter<BusinessGuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessGuideViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.business_guide_item_layout, parent, false)
        return BusinessGuideViewHolder(view)
    }

    override fun getItemCount(): Int {
        return businessGuideList.size
    }

    override fun onBindViewHolder(holder: BusinessGuideViewHolder, position: Int) {
        val poJo = businessGuideList[position]
        holder.bind(poJo)

        holder.itemView.setOnClickListener {
            IntentHelper.startBusinessGuideDetailsActivity(context = context, businessGuide = poJo)
        }

    }

}
