package com.almersal.android.listeners

import com.almersal.android.data.entities.Category

/**
 * Created by Adhamkh on 2018-07-26.
 */
interface OnCategorySelectListener {
    fun onSelectCategory(category: Category)
    fun onDeselectCategory(category: Category) {}
}