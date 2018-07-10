package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.listeners.OnTagSelectListener
import com.brainsocket.globalpages.viewHolders.TagEntityViewHolder

/**
 * Created by Adhamkh on 2018-07-04.
 */
class TagsRecyclerViewAdapter constructor(var context: Context, var tagsListList: MutableList<TagEntity>) :
        RecyclerView.Adapter<TagEntityViewHolder>() {

    var onTagSelectListener: OnTagSelectListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagEntityViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.tag_item_layout, parent, false)
        return TagEntityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tagsListList.size
    }

    override fun onBindViewHolder(holder: TagEntityViewHolder, position: Int) {
        var pojo = tagsListList[holder.adapterPosition]
        holder.bind(pojo)
        holder.tag_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && buttonView != null) {
                tagsListList.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                onTagSelectListener?.onSelectTag(pojo)
            }
        }
    }

    fun addTag(tagEntity: TagEntity) {
        tagsListList.add(tagEntity)
        notifyItemInserted(tagsListList.indexOf(tagEntity))
    }

}
