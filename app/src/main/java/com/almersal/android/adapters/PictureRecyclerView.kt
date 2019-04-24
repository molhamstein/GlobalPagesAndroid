package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.almersal.android.R
import com.almersal.android.data.entities.MediaEntity
import com.almersal.android.viewHolders.PictureViewHolder


class PictureRecyclerView constructor(var context: Context, var mediaList: MutableList<MediaEntity>) :
        RecyclerView.Adapter<PictureViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.picture_item_layout, parent, false)
        return PictureViewHolder(view)
    }

    override fun getItemCount(): Int = mediaList.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val poJo = mediaList[position]
        holder.bind(poJo)
    }

}
