package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.ProductViewHolder

/**
 * Created by Adhamkh on 2018-10-07.
 */
class BusinessGuideProductRecyclerViewAdapter constructor(var context: Context, private var productThumbList: MutableList<ProductThumb>
                                                          , private var isOwner: Boolean, private val businessGuide: BusinessGuide,
                                                          private var isVertical: Boolean = false)
    : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false)
//        if (isVertical) {
//            val lp = view.layoutParams as ViewGroup.LayoutParams
//            lp.width = 3 * parent.measuredWidth / 4
//            view.layoutParams = lp
//        }
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

    fun addOrUpdateItem(productThumb: ProductThumb) {
        val sz = productThumbList.size - 1
        for (i: Int in (0..sz)) {
            if (productThumbList[i].id == (productThumb.id)) {
                productThumbList[i] = productThumb
                notifyDataSetChanged()
                return
            }
        }
        productThumbList.add(productThumb)
        notifyDataSetChanged()
    }

}
