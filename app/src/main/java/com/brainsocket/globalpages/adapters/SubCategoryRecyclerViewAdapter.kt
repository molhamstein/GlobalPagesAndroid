package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.SubCategory
import com.brainsocket.globalpages.listeners.OnSubCategorySelectListener
import com.brainsocket.globalpages.viewHolders.SubCategoryViewHolder

/**
 * Created by Adhamkh on 2018-07-03.
 */
class SubCategoryRecyclerViewAdapter constructor(var context: Context, var subCategoriesList: MutableList<SubCategory>,
                                                 var onSubCategorySelectListener: OnSubCategorySelectListener? = null) :
        RecyclerView.Adapter<SubCategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.subcategory_item_layout, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subCategoriesList.size
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        var pojo = subCategoriesList[position]
        holder.bind(pojo)
        holder.itemView.findViewById<ToggleButton>(R.id.subCategory_toggle)
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (isChecked) {
                            setCheck(pojo)
                            onSubCategorySelectListener?.onSelectSubCategory(pojo)
                        } else {
                            buttonView?.setOnCheckedChangeListener(null)
                            pojo.isSelected = false
                            buttonView?.isChecked = pojo.isSelected
                            buttonView?.setOnCheckedChangeListener(this)
                        }
                    }
                })

    }

    fun setCheck(subCategory: SubCategory) {
        subCategoriesList.forEach {
            it.isSelected = (it == subCategory)
        }
        notifyDataSetChanged()
    }

}