package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.Attachment
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.viewHolders.AttachmentViewHolder

class VideoAttachmentRecyclerViewAdapter constructor(var context: Context, var attachmentList: MutableList<Attachment>)
    : RecyclerView.Adapter<AttachmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.attachment_item_layout, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return attachmentList.size
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val poJo = attachmentList[position]
        holder.bind(poJo)
        holder.mediaRemoveAction.setOnClickListener {
            attachmentList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
        BindingUtils.loadAttachmentImage(holder.attachmentImage, poJo)
//        holder.itemView.setOnClickListener {
        //            intentHelper.startSearchMapActivity(context)
//        }

    }

    fun addItem(attachment: Attachment) {
        attachmentList.add(attachment)
        notifyItemInserted(attachmentList.size - 1)
    }

}
