package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.Attachment

/**
 * Created by Adhamkh on 2018-07-19.
 */
class AttachmentViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.media_remove_action)
    lateinit var mediaRemoveAction: ImageView

    @BindView(R.id.attachmentImage)
    lateinit var attachmentImage: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(attachment: Attachment) {

    }

}