package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.listeners.OnBusinessSelectListener
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.viewHolders.BusinessViewHolder
import kotlinx.android.synthetic.main.business_item_layout.*

class BusinessSelectorAdapter constructor(
    var context: Context, var data: MutableList<BusinessGuide>
    , var onCitySelectListener: OnBusinessSelectListener? = null
) :
    RecyclerView.Adapter<BusinessViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.business_item_layout, parent, false)
        return BusinessViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        var pojo = data[position]
        holder.bind(pojo)
        holder.business_toggle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    setCheck(pojo)
                    onCitySelectListener?.onSelectBusiness(pojo)
                } else {
                    buttonView?.setOnCheckedChangeListener(null)
                    pojo.isSelected = false
                    buttonView?.isChecked = pojo.isSelected
                    buttonView?.setOnCheckedChangeListener(this)
                }
            }
        })


    }


    fun setCheck(city: BusinessGuide): Int {
        var index = -1
        val sz = data.size - 1
        for (i in 0..(sz)) {
            val it = data[i]
            if (it == city)
                index = i
            it.isSelected = (it == city)
        }
        notifyDataSetChanged()
        return index
    }

    fun getCurrentCity(): BusinessGuide? {
        data.forEach {
            if (it.isSelected)
                return it
        }
        return null
    }

}