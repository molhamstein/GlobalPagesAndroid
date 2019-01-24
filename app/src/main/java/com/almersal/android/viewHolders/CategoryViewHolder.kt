package com.almersal.android.viewHolders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.Category

class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val context = view.context

    init {
        ButterKnife.bind(this, view)

    }

    @BindView(R.id.category_toggle)
    lateinit var categoryToggle: ToggleButton


    fun bind(category: Category, isTransparent: Boolean) {
        if (isTransparent)
            categoryToggle.background = ContextCompat.getDrawable(context, R.drawable.ic_cat_tag_transparent_bg)
        categoryToggle.textOff = category.getTitle()
        categoryToggle.textOn = category.getTitle()
        categoryToggle.text = category.getTitle()
        categoryToggle.setOnCheckedChangeListener(null)
        categoryToggle.isChecked = category.isSelected
    }
}