package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.City

/**
 * Created by Adhamkh on 2018-07-03.
 */
class CityViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.city_toggle)
    lateinit var city_toggle: ToggleButton

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(city: City) {
        city_toggle.textOff = city.getTitle()
        city_toggle.textOn = city.getTitle()
        city_toggle.text = city.getTitle()
    }
}