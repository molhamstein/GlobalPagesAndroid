package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.globalpages.viewHolders.PostSliderViewHolder

class PostSliderRecyclerViewAdapter constructor(var context: Context, var postList: MutableList<Post>)
    : RecyclerView.Adapter<PostSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostSliderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_featured_item_layout, parent, false)
        return PostSliderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostSliderViewHolder, position: Int) {
        val poJo = postList[position]
        holder.bind(poJo)

        holder.itemView.setOnClickListener {
            IntentHelper.startPostDetailsActivity(context, poJo)
        }
    }

}