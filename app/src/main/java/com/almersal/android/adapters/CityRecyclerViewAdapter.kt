package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.City
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.viewHolders.CityViewHolder


class CityRecyclerViewAdapter constructor(var context: Context, var citiesListList: MutableList<City>
                                          , var onCitySelectListener: OnCitySelectListener? = null) :
        RecyclerView.Adapter<CityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.city_item_layout, parent, false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return citiesListList.size
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        var pojo = citiesListList[position]
        holder.bind(pojo)
        holder.itemView.findViewById<ToggleButton>(R.id.city_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(pojo)
                            onCitySelectListener?.onSelectCity(pojo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            pojo.isSelected = false
                            buttonView?.isChecked = pojo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })

    }

    fun setCheck(city: City): Int {
        var index = -1
        val sz = citiesListList.size - 1
        for (i in 0..(sz)) {
            val it = citiesListList[i]
            if (it == city)
                index = i
            it.isSelected = (it == city)
        }
        notifyDataSetChanged()
        return index
    }

    fun getCurrentCity(): City? {
        citiesListList.forEach {
            if (it.isSelected)
                return it
        }
        return null
    }

}