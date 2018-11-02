package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Attachment
import com.brainsocket.globalpages.utilities.BindingUtils
import com.brainsocket.globalpages.viewHolders.AttachmentViewHolder

class AttachmentRecyclerViewAdapter constructor(var context: Context, var attachmentList: MutableList<Attachment>)
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
