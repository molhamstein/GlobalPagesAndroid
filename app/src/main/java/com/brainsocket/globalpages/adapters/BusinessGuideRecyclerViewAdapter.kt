package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.viewHolders.BusinessGuideViewHolder

/**
 * Created by Adhamkh on 2018-07-18.
 */
class BusinessGuideRecyclerViewAdapter constructor(var context: Context, var businessGuideList: MutableList<BusinessGuide>)
    : RecyclerView.Adapter<BusinessGuideViewHolder>() {

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
            //            intentHelper.startSearchMapActivity(context)
        }
    }

}
