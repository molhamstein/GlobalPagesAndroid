package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.viewHolders.CategoryViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class CategoryRecyclerViewAdapter constructor(var context: Context, var categoriesList: MutableList<Category>) :
        RecyclerView.Adapter<CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var pojo = categoriesList[position]
        holder.bind(pojo)
        holder.itemView.findViewById<ToggleButton>(R.id.category_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(pojo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            pojo.isSelected = false
                            buttonView?.isChecked = pojo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })

    }

    fun setCheck(category: Category) {
        categoriesList.forEach {
            it.isSelected = (it == category)
        }
        notifyDataSetChanged()
    }

//    fun getCurrentCategory(): Category {
//        categoriesList.forEach {
//            if (it.isSelected)
//                return it
//        }
//        return GeneralRepositories.getDefaultCategory()
//    }

}