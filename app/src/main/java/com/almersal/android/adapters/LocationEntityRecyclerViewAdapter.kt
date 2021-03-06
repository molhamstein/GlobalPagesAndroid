package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.City
import com.almersal.android.data.entities.LocationEntity
import com.almersal.android.viewHolders.LocationEntityViewHolder

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
        holder.itemView.findViewById<ToggleButton>(R.id.location_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(pojo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            pojo.isSelected = false
                            buttonView?.isChecked = pojo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })
    }

    fun setCheck(locationEntity: LocationEntity): Int {
        var index = -1
        val sz = locationsListList.size - 1
        for (i in 0..(sz)) {
            val it = locationsListList[i]
            if (it == locationEntity)
                index = i
            it.isSelected = (it == locationEntity)
        }
        notifyDataSetChanged()
        return index
    }

    fun getCurrentLocation(): LocationEntity? {
        locationsListList.forEach {
            if (it.isSelected)
                return it
        }
        return null
    }

}