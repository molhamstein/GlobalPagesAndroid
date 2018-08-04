package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.viewHolders.BusinessGuideSliderViewHolder

/**
 * Created by Adhamkh on 2018-06-28.
 */
class BusinessGuideSliderRecyclerViewAdapter constructor(var context: Context, var businessGuideList: MutableList<BusinessGuide>)
    : RecyclerView.Adapter<BusinessGuideSliderViewHolder>() {


//    init {
//        businessGuideList = mutableListOf()
//        businessGuideList.add(BusinessGuideEntity(ContextCompat.getColor(context, R.color.businessGuideColor1),
//                R.drawable.ic_nearby_24dp, R.string.businessGuideTitle1,
//                R.string.businessGuideDetails1))
//        businessGuideList.add(BusinessGuideEntity(ContextCompat.getColor(context, R.color.businessGuideColor2),
//                R.drawable.ic_nearby_24dp, R.string.businessGuideTitle2,
//                R.string.businessGuideDetails2))
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessGuideSliderViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.business_guide_entity_item_layout, parent, false)
        return BusinessGuideSliderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return businessGuideList.size
    }

    override fun onBindViewHolder(holder: BusinessGuideSliderViewHolder, position: Int) {
        var pojo = businessGuideList[position]
        holder.bind(pojo)

        holder.itemView.setOnClickListener {
            //            intentHelper.startSearchMapActivity(context)
        }
    }

}