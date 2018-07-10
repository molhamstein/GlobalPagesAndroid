package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.viewHolders.SubCategoryViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class SubCategoryRecyclerViewAdapter  constructor(var context: Context, var subCategoriesListList: MutableList<SubCategory>) :
        RecyclerView.Adapter<SubCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.subcategory_item_layout, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subCategoriesListList.size
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        var pojo = subCategoriesListList[position]
        holder.bind(pojo)

    }

}