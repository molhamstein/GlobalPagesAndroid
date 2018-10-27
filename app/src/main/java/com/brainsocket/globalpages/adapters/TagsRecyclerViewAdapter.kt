package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.listeners.OnTagSelectListener
import com.brainsocket.globalpages.viewHolders.TagEntityViewHolder

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
            tagsListList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            onTagSelectListener?.onSelectTag(poJo)
            Log.v("", "")
        }
        holder.tag_toggle.setOnClickListener {
            onTagSelectListener?.onTagClick(poJo)
        }

//        var listener = object : RightDrawableOnTouchListener(holder.tag_toggle) {
//            override fun onDrawableTouch(event: MotionEvent?): Boolean {
//                tagsListList.removeAt(holder.adapterPosition)
//                notifyItemRemoved(holder.adapterPosition)
//                onTagSelectListener?.onSelectTag(pojo)
//                Log.v("", "")
//                return false
//            }
//        }
//
//        holder.tag_toggle.setOnTouchListener(listener)
//        holder.tag_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked && buttonView != null) {
//                tagsListList.removeAt(holder.adapterPosition)
//                notifyItemRemoved(holder.adapterPosition)
//                onTagSelectListener?.onSelectTag(pojo)
//            }
//        }

    }

    fun addTag(tagEntity: TagEntity) {
        tagsListList.add(tagEntity)
        notifyItemInserted(tagsListList.indexOf(tagEntity))
    }

}
