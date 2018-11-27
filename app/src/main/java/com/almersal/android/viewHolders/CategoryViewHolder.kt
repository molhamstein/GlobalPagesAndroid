package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.Category

/**
 * Created by Adhamkh on 2018-07-03.
 */
class CategoryViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.category_toggle)
    lateinit var category_toggle: ToggleButton

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(category: Category) {
        category_toggle.textOff = category.getTitle()
        category_toggle.textOn = category.getTitle()
        category_toggle.text = category.getTitle()
        category_toggle.setOnCheckedChangeListener(null)
        category_toggle.isChecked = category.isSelected
    }
}