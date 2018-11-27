package com.almersal.android.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entitiesModel.OpenDayModel

/**
 * Created by Adhamkh on 2018-10-11.
 */
class OpenDayViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.openDayCheckBox)
    lateinit var openDayCheckBox: CheckBox

    @BindView(R.id.openDayName)
    lateinit var openDayName: TextView

    var context: Context

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun bind(openDayModel: OpenDayModel) {
        openDayCheckBox.isChecked = openDayModel.isSelected
        openDayName.text = context.resources.getString(openDayModel.getOpenDayStringName())
    }

}