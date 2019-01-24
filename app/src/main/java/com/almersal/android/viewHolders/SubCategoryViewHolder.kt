package com.almersal.android.viewHolders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.SubCategory

class SubCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.subCategory_toggle)
    lateinit var subCategoryToggle: ToggleButton

    private val context = view.context

    init {
        ButterKnife.bind(this, view)
    }

    fun bind(subCategory: SubCategory, isTransparent: Boolean) {
        if (isTransparent)
            subCategoryToggle.background = ContextCompat.getDrawable(context, R.drawable.ic_cat_tag_transparent_bg)
        subCategoryToggle.textOff = subCategory.getTitle()
        subCategoryToggle.textOn = subCategory.getTitle()
        subCategoryToggle.text = subCategory.getTitle()
        subCategoryToggle.setOnCheckedChangeListener(null)
        subCategoryToggle.isChecked = subCategory.isSelected
    }

}