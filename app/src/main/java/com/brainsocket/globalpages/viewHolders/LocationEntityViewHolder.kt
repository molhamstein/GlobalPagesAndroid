package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.LocationEntity

/**
 * Created by Adhamkh on 2018-07-03.
 */
class LocationEntityViewHolder : RecyclerView.ViewHolder {
    @BindView(R.id.location_toggle)
    lateinit var locationToggle: ToggleButton

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(locationEntity: LocationEntity) {
        locationToggle.textOff = locationEntity.getTitle()
        locationToggle.textOn = locationEntity.getTitle()
        locationToggle.text = locationEntity.getTitle()
        locationToggle.setOnCheckedChangeListener(null)
        locationToggle.isChecked = locationEntity.isSelected
    }
}