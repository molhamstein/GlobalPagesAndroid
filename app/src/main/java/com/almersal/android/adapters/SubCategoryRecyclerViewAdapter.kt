package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.SubCategory
import com.almersal.android.listeners.OnSubCategorySelectListener
import com.almersal.android.viewHolders.SubCategoryViewHolder

class SubCategoryRecyclerViewAdapter constructor(var context: Context, var subCategoriesList: MutableList<SubCategory>,
                                                 var isTransparent: Boolean = false,
                                                 var onSubCategorySelectListener: OnSubCategorySelectListener? = null) :
        RecyclerView.Adapter<SubCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.subcategory_item_layout, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subCategoriesList.size
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val poJo = subCategoriesList[position]
        holder.bind(poJo, isTransparent)
        holder.itemView.findViewById<ToggleButton>(R.id.subCategory_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(poJo)
                            onSubCategorySelectListener?.onSelectSubCategory(poJo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            poJo.isSelected = false
                            buttonView?.isChecked = poJo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                            onSubCategorySelectListener?.onUnSelectSubCategory(poJo)
                        }
                    }
                })

    }

    fun setCheck(subCategory: SubCategory): Int {
        var index = -1
        val sz = subCategoriesList.size - 1
        for (i in 0..(sz)) {
            val it = subCategoriesList[i]
            if (it == subCategory)
                index = i
            it.isSelected = (it == subCategory)
        }
        notifyDataSetChanged()
        return index
    }


    fun setCheck(subCategoryId: String) {
        subCategoriesList.forEach {
            if (it.id == subCategoryId)
                it.isSelected = true
        }
        notifyDataSetChanged()
    }

    fun getCurrentSubCategory(): SubCategory? {
        subCategoriesList.forEach {
            if (it.isSelected)
                return it
        }
        return null
    }

    fun setSubCategoryItemStatus(subCategory: SubCategory, isChecked: Boolean) {
        subCategoriesList.forEach {
            if (subCategory.id == it.id) {
                it.isSelected = isChecked
                notifyDataSetChanged()
            }
        }
    }

}