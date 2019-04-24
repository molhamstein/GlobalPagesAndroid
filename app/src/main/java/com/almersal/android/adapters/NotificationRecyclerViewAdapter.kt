package com.almersal.android.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.almersal.android.R
import com.almersal.android.data.entities.NotificationEntity
import com.almersal.android.enums.NotificationsTypeEnum
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewHolders.NotificationViewHolder

class NotificationRecyclerViewAdapter constructor(val context: Context, private val notificationList: MutableList<NotificationEntity>) :
        RecyclerView.Adapter<NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val mView = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false)
        return NotificationViewHolder(mView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val poJo: NotificationEntity = notificationList[position]
        holder.bind(poJo)
        holder.itemView.setOnClickListener {
            when (poJo._type) {
                NotificationsTypeEnum.ADD_NEW_VOLUME.type -> {
                    IntentHelper.startVolumesActivity(context, poJo.data.volumeId)
                }
            }
        }
    }

    override fun getItemCount(): Int = notificationList.size

}