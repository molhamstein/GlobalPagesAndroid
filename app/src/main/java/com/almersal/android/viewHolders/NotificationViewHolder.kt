package com.almersal.android.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.NotificationEntity
import com.almersal.android.enums.NotificationsTypeEnum
import com.almersal.android.normalization.DateNormalizer

class NotificationViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.notificationName)
    lateinit var notificationName: TextView

    @BindView(R.id.notificationDate)
    lateinit var notificationDate: TextView

    var context: Context

    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun bind(notificationEntity: NotificationEntity) {
        when (notificationEntity._type) {
            NotificationsTypeEnum.ADD_NEW_VOLUME.type -> {
                notificationName.text = context.getString(R.string.NewVolumeAdded)
            }
            else -> {
                notificationName.text = notificationEntity.message
            }
        }
        notificationDate.text = DateNormalizer
                .getCanonicalDateTime(notificationEntity.creationDate)
    }

}