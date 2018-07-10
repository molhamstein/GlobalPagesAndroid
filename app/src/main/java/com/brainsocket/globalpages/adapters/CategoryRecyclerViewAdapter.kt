package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.viewHolders.CategoryViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class CategoryRecyclerViewAdapter constructor(var context: Context, var categoriesListList: MutableList<Category>) :
        RecyclerView.Adapter<CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesListList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var pojo = categoriesListList[position]
        holder.bind(pojo)

    }

}