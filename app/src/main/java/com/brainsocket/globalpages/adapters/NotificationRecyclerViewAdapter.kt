package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.NotificationEntity
import com.brainsocket.globalpages.viewHolders.NotificationViewHolder

class NotificationRecyclerViewAdapter constructor(val context: Context, private val notificationList: MutableList<NotificationEntity>) :
        RecyclerView.Adapter<NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false)
        return NotificationViewHolder(view)
    }


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val poJo: NotificationEntity = notificationList[position]
        holder.bind(poJo)
    }

    override fun getItemCount(): Int = notificationList.size

}