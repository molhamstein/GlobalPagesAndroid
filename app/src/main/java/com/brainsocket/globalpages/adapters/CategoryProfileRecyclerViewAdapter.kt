package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.globalpages.viewHolders.CategoryViewHolder

class CategoryProfileRecyclerViewAdapter constructor(var context: Context, var categoriesList: MutableList<Category>
                                                     , var onCategorySelectListener: OnCategorySelectListener? = null,
                                                     var isClickable: Boolean = true) :
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
        holder.bind(poJo)
        holder.itemView.findViewById<ToggleButton>(R.id.category_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (!isClickable) {
                            holder.itemView.findViewById<ToggleButton>(R.id.category_toggle).isChecked = !isChecked
                            return
                        }
                        if (isChecked) {
                            poJo.isSelected = true
                            onCategorySelectListener?.onSelectCategory(poJo)
                        } else {
                            onCategorySelectListener?.onDeselectCategory(poJo)
                            buttonView?.setOnCheckedChangeListener(null)
                            poJo.isSelected = false
                            buttonView?.isChecked = poJo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })

    }

    fun getSelectedCategories(): MutableList<Category> {
        val selectedList: MutableList<Category> = mutableListOf()
        categoriesList.forEach {
            if (it.isSelected)
                selectedList.add(it)
        }
        return selectedList
    }

    fun setCategoryItemStatus(category: Category, isChecked: Boolean) {
        categoriesList.forEach {
            if (category.id == it.id) {
                it.isSelected = isChecked
                notifyDataSetChanged()
            }
        }
    }

}