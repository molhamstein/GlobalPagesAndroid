package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.ProductThumb
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.globalpages.viewHolders.ProductViewHolder

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductRecyclerViewAdapter constructor(var context: Context, private var productThumbList: MutableList<ProductThumb>
                                                          , private var isOwner: Boolean, private val businessGuide: BusinessGuide)
    : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productThumbList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val poJo = productThumbList[position]
        holder.bind(poJo, isOwner)

        holder.itemView.setOnClickListener {
            IntentHelper.startProductDetailsActivity(context = context, productThumb = poJo)
        }
        holder.editProduct.setOnClickListener {
            IntentHelper.startProductAddActivity(context = context, businessGuide = businessGuide, productThumb = poJo)
        }

    }

}
