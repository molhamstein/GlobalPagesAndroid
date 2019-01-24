package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.Category
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.viewHolders.CategoryViewHolder

class CategoryRecyclerViewAdapter constructor(var context: Context, var categoriesList: MutableList<Category>,
                                              var isTransparent: Boolean = false,
                                              var onCategorySelectListener: OnCategorySelectListener? = null) :
        RecyclerView.Adapter<CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val poJo = categoriesList[position]
        holder.bind(poJo, isTransparent)
        holder.itemView.findViewById<ToggleButton>(R.id.category_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(poJo)
                            onCategorySelectListener?.onSelectCategory(poJo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            poJo.isSelected = false
                            buttonView?.isChecked = poJo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })

    }

    fun setCheck(category: Category): Int {
        var index = -1
        val sz = categoriesList.size - 1
        for (i in 0..(sz)) {
            val it = categoriesList[i]
            if (it == category)
                index = i
            it.isSelected = (it == category)
        }
        notifyDataSetChanged()
        return index
    }

    fun getCurrentCategory(): Category? {
        categoriesList.forEach {
            if (it.isSelected)
                return it
        }
        return null
    }

}