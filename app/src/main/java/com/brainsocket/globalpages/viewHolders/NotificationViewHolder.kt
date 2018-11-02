package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.NotificationEntity

class NotificationViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.notificationName)
    lateinit var notificationName: TextView

    init {
        ButterKnife.bind(this, view)
    }

    fun bind(notificationEntity: NotificationEntity) {
        notificationName.text = notificationEntity.message
    }

}