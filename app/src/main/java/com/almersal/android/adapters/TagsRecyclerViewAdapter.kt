package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.TagEntity
import com.almersal.android.enums.TagType
import com.almersal.android.listeners.OnTagSelectListener
import com.almersal.android.viewHolders.TagEntityViewHolder

class TagsRecyclerViewAdapter constructor(var context: Context, var tagsListList: MutableList<TagEntity>,
                                          private var withClose: Boolean = true, var onTagSelectListener: OnTagSelectListener? = null) :
        RecyclerView.Adapter<TagEntityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagEntityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tag_item_layout, parent, false)
        return TagEntityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tagsListList.size
    }

    override fun onBindViewHolder(holder: TagEntityViewHolder, position: Int) {
        val poJo = tagsListList[holder.adapterPosition]
        holder.bind(poJo)


        if (!withClose) {
            holder.tag_close.visibility = View.GONE
//            holder.tag_toggle.setCompoundDrawables(null, null, null, null)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                holder.tag_toggle.setCompoundDrawablesRelative(null, null, null, null)
//            }
        } else {
            holder.tag_close.visibility = View.VISIBLE
            holder.tag_close.bringToFront()
        }


        holder.tag_close.setOnClickListener {
            if (poJo.tagId.isBlank() && (poJo.tagType != TagType.Query))
                return@setOnClickListener
            tagsListList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            onTagSelectListener?.onSelectTag(poJo)
            Log.v("", "")
        }

        holder.itemView.setOnClickListener {
            onTagSelectListener?.onTagClick(poJo)
        }

    }

    fun addTag(tagEntity: TagEntity) {
        tagsListList.add(tagEntity)
        notifyItemInserted(tagsListList.indexOf(tagEntity))
    }

}
