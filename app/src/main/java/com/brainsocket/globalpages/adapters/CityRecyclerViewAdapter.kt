package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.viewHolders.CityViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class CityRecyclerViewAdapter constructor(var context: Context, var citiesListList: MutableList<City>) :
        RecyclerView.Adapter<CityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.city_item_layout, parent, false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return citiesListList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        var pojo = citiesListList[position]
        holder.bind(pojo)

    }

}