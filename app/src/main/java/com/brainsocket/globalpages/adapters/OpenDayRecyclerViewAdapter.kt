package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.data.entitiesModel.OpenDayModel
import com.brainsocket.globalpages.viewHolders.CityViewHolder
import com.brainsocket.globalpages.viewHolders.OpenDayViewHolder

class OpenDayRecyclerViewAdapter constructor(var context: Context, var openDayList: MutableList<OpenDayModel>) :
        RecyclerView.Adapter<OpenDayViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenDayViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.open_day_item_layout, parent, false)
        return OpenDayViewHolder(view)
    }

    override fun getItemCount(): Int = openDayList.size

    override fun onBindViewHolder(holder: OpenDayViewHolder, position: Int) {
        val poJo = openDayList[position]
        holder.bind(poJo)

        holder.openDayCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                poJo.isSelected = isChecked
            }
        })
    }

    public fun getSelectedOpenDayList(): MutableList<OpenDayModel> {
        val selectedList = mutableListOf<OpenDayModel>()
        openDayList.forEach {
            if (it.isSelected)
                selectedList.add(it)
        }
        return selectedList
    }


}