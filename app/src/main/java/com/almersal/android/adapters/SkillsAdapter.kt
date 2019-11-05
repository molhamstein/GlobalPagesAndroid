package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Tag
import com.almersal.android.viewHolders.SkillViewHolder
import kotlinx.android.synthetic.main.skill_item_layout.*

class SkillsAdapter(
    var context: Context,
    var data: MutableList<Tag>,
    var removeEnabled: Boolean
) : RecyclerView.Adapter<SkillViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.skill_item_layout, parent, false)
        return SkillViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        holder.tagName.text = data[position].name
        if (removeEnabled) holder.removeBtn.visibility = View.VISIBLE
        else holder.removeBtn.visibility = View.GONE

        holder.removeBtn.setOnClickListener {
            removeItem(position)
        }
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(0,data.size)
    }

    fun addItem(tag: Tag?) {
        if (tag != null) {
            if (!data.contains(tag)) {
                data.add(tag)
                notifyDataSetChanged()
            }
        }
    }
}