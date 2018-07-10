package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuideEntity
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.viewHolders.BusinessGuideViewHolder

/**
 * Created by Adhamkh on 2018-06-28.
 */
class BusinessGuideRecyclerViewAdapter constructor(var context: Context) : RecyclerView.Adapter<BusinessGuideViewHolder>() {

    lateinit var businessGuideList: MutableList<BusinessGuideEntity>

    init {
        businessGuideList = mutableListOf()
        businessGuideList.add(BusinessGuideEntity(context.resources.getColor(R.color.businessGuideColor1),
                R.drawable.ic_nearby_24dp, R.string.businessGuideTitle1,
                R.string.businessGuideDetails1))
        businessGuideList.add(BusinessGuideEntity(context.resources.getColor(R.color.businessGuideColor2),
                R.drawable.ic_nearby_24dp, R.string.businessGuideTitle2,
                R.string.businessGuideDetails2))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessGuideViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.business_guide_item_layout, parent, false)
        return BusinessGuideViewHolder(view)
    }

    override fun getItemCount(): Int {
        return businessGuideList.size
    }

    override fun onBindViewHolder(holder: BusinessGuideViewHolder, position: Int) {
        var pojo = businessGuideList[position]
        holder.bind(pojo)

        holder.itemView.setOnClickListener {
            intentHelper.startSearchMapActivity(context)
        }
    }

}