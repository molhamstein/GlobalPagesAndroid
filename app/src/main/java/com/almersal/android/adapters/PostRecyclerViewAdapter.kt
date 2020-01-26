package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Post
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.TagEntity
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.enums.PostType
import com.almersal.android.enums.TagType
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.PostViewHolder


class PostRecyclerViewAdapter @JvmOverloads constructor(
    var context: Context, private var postsList: MutableList<Post> = mutableListOf(),
    private var isFixed: Boolean = false,
    private var postFilterList: MutableList<Post>? = null,
    private var products: MutableList<Product>? = null
) :
    RecyclerView.Adapter<PostViewHolder>() {

    var filterEntity: FilterEntity? = null

    init {
        postFilterList = postsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.post_item_layout, parent, false)

        if (isFixed)
            view = LayoutInflater.from(context).inflate(R.layout.post_item_fixed_layout, parent, false)
        when (viewType) {

            PostType.WITH_TEXT.type -> {
                view = LayoutInflater.from(context).inflate(R.layout.post_item_text_layout, parent, false)
            }
            PostType.WITH_IMAGE_VERTICAL.type -> {
                view = LayoutInflater.from(context).inflate(R.layout.post_item_fixed_layout, parent, false)
            }
            PostType.WITH_TEXT_VERTICAL.type -> {
                view = LayoutInflater.from(context).inflate(R.layout.post_item_text_fixed_layout, parent, false)
            }
            PostType.ONLY_IMAGE.type -> {
                if (!isFixed)
                    view = LayoutInflater.from(context).inflate(R.layout.product_main_item_layout, parent, false)
                else
                    view = LayoutInflater.from(context).inflate(R.layout.product_main_fixed_layout, parent, false)
            }
        }

        return PostViewHolder(view)
    }

    fun addProducts(data: List<Product>) {
        if (products == null)
            products = mutableListOf()
        products!!.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (products != null) products!!.size else postsList.size

    override fun getItemViewType(position: Int): Int {
        return if (products != null)
            products!![position].getPostType()
        else
            postsList[position].getPostType()
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        if (products != null) {
            val poJo = products!![position]
            holder.bind(poJo)

            holder.itemView.setOnClickListener {
                //            IntentHelper.startPostDetailsActivity(context, poJo)
                IntentHelper.startProductDetailsActivity(context, poJo.id)
            }
        } else {
            val poJo = postsList[position]
            holder.bind(poJo)

            holder.itemView.setOnClickListener {
                //            IntentHelper.startPostDetailsActivity(context, poJo)
                IntentHelper.startPostDetailsActivity(context, poJo.id)
            }
        }
    }

    fun filterByCriteria(filterEntity: FilterEntity, flagMarket: Boolean) {
        this.filterEntity = filterEntity

        if (!flagMarket) {

            if (postFilterList == null)
                postFilterList = postsList

            postsList = postFilterList!!.filter {
                var result = true
                if ((filterEntity.query != null) and (!filterEntity.query.isNullOrEmpty()))
                    result = result and it.title.contains(filterEntity.query!!, false)
                if (filterEntity.category != null)
                    result = result and (it.category == filterEntity.category)
                if (filterEntity.subCategory != null)
                    result = result and (it.subCategory == filterEntity.subCategory)
                if (filterEntity.city != null)
                    result = result and (it.city == filterEntity.city)
                if (filterEntity.area != null)
                    result = result and (it.location == filterEntity.area)
                result
            }.toMutableList()

            notifyDataSetChanged()
        }
    }

    fun excludeFilter(tagEntity: TagEntity,flagMarket: Boolean) {
        if (filterEntity == null)
            return

        when (tagEntity.tagType) {
            TagType.Category -> {
                filterEntity!!.category = null
            }
            TagType.SubCategory -> {
                filterEntity!!.subCategory = null
            }
            TagType.City -> {
                filterEntity!!.city = null
            }
            TagType.Location -> {
                filterEntity!!.area = null
            }
            TagType.Query -> {
                filterEntity!!.query = null
            }
        }
        filterByCriteria(filterEntity!!,flagMarket)
    }

}