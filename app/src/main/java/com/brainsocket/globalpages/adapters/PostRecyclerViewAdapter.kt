package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.data.filtration.FilterEntity
import com.brainsocket.globalpages.enums.PostType
import com.brainsocket.globalpages.enums.TagType
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.globalpages.viewHolders.PostViewHolder

/**
 * Created by Adhamkh on 2018-06-29.
 */
class PostRecyclerViewAdapter constructor(var context: Context, var postsList: MutableList<Post>, private var isFixed: Boolean = false,
                                          var postFilterList: MutableList<Post>? = null) :
        RecyclerView.Adapter<PostViewHolder>() {

    var filterEntity: FilterEntity? = null

    init {
        postFilterList = postsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.post_item_layout, parent, false)

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
        }

        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return postsList[position].getPostType() + (if (isFixed) 2 else 0)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var pojo = postsList[position]
        holder.bind(pojo)

        holder.itemView.setOnClickListener {
            IntentHelper.startPostDetailsActivity(context, pojo)
        }
    }

    fun filterByCreteria(filterEntity: FilterEntity) {
        this.filterEntity = filterEntity

        if (postFilterList == null)
            postFilterList = postsList

        postsList = postFilterList!!.filter {
            var result = true
            if ((filterEntity.query != null) and (!filterEntity.query.isNullOrEmpty()))
                result = result and it.title.contains(filterEntity.query!!, false)
            if (filterEntity.postCategory != null)
                result = result and (it.category == filterEntity.postCategory)
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

    fun excludeFilter(tagEntity: TagEntity) {
        if (filterEntity == null)
            return

        when (tagEntity.tagType) {
            TagType.Category -> {
                filterEntity!!.postCategory = null
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
        }
        filterByCreteria(filterEntity!!)
    }

}