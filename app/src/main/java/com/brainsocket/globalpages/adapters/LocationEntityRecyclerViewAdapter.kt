package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.LocationEntity
import com.brainsocket.globalpages.viewHolders.LocationEntityViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class LocationEntityRecyclerViewAdapter constructor(var context: Context, var locationsListList: MutableList<LocationEntity>) :
        RecyclerView.Adapter<LocationEntityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationEntityViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.location_item_layout, parent, false)
        return LocationEntityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationsListList.size
    }

    override fun onBindViewHolder(holder: LocationEntityViewHolder, position: Int) {
        var pojo = locationsListList[position]
        holder.bind(pojo)

    }

}