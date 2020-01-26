package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.ProductViewHolder
import kotlinx.android.synthetic.main.product_main_fixed_layout.*


class ProductsRecyclerViewAdapter constructor(
    var context: Context, private var productList: MutableList<Product>
    , private var isOwner: Boolean = false, private val businessGuide: BusinessGuide? = null,
    private var isVertical: Boolean = false
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_main_fixed_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val poJo = productList[position]
        holder.bind(poJo)

        holder.itemView.setOnClickListener {
            IntentHelper.startProductDetailsActivity(context, poJo.id)
        }
        holder.editProduct.visibility = View.VISIBLE
        holder.editProduct.setOnClickListener {
            IntentHelper.startProductAddActivity(context = context, businessId = businessGuide?.id, product = poJo)
        }

    }

    fun addOrUpdateItem(productThumb: Product) {
        val sz = productList.size - 1
        for (i: Int in (0..sz)) {
            if (productList[i].id == (productThumb.id)) {
                productList[i] = productThumb
                notifyDataSetChanged()
                return
            }
        }
        productList.add(productThumb)
        notifyDataSetChanged()
    }

}
