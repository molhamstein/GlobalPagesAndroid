package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.SubCategory

/**
 * Created by Adhamkh on 2018-07-03.
 */
class SubCategoryViewHolder : RecyclerView.ViewHolder {
    @BindView(R.id.subCategory_toggle)
    lateinit var subCategory_toggle: ToggleButton

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(subCategory: SubCategory) {
        subCategory_toggle.textOff = subCategory.getTitle()
        subCategory_toggle.textOn = subCategory.getTitle()
        subCategory_toggle.text = subCategory.getTitle()
        subCategory_toggle.setOnCheckedChangeListener(null)
        subCategory_toggle.isChecked = subCategory.isSelected
    }
}