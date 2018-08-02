package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.enums.PostType
import com.brainsocket.globalpages.utilities.BindingUtils
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.viewHolders.PostViewHolder

/**
 * Created by Adhamkh on 2018-06-29.
 */
class PostRecyclerViewAdapter constructor(var context: Context, var postsList: MutableList<Post>, private var isFixed: Boolean = false) :
        RecyclerView.Adapter<PostViewHolder>() {


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
            intentHelper.startPostDetailsActivity(context, pojo.id)
        }

    }

}