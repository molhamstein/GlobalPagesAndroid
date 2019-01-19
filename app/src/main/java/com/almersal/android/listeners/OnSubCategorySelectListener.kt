package com.almersal.android.listeners

import com.almersal.android.data.entities.SubCategory


interface OnSubCategorySelectListener {
    fun onSelectSubCategory(subCategory: SubCategory)

    fun onUnSelectSubCategory(subCategory: SubCategory){}
}