package com.almersal.android.viewHolders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.MediaEntity
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper


class PictureViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    var context: Context

    @BindView(R.id.pictureImage)
    lateinit var pictureImage: ImageView

    @BindView(R.id.videoImageView)
    lateinit var videoImageView: ImageView


    init {
        ButterKnife.bind(this, view)
        context = view.context
    }

    fun bind(poJo: MediaEntity) {
        if (poJo.url.endsWith("mp4", true))
            videoImageView.visibility = View.VISIBLE
        else
            videoImageView.visibility = View.GONE

        var url = poJo.url
        if (videoImageView.visibility == View.VISIBLE) {
            url = poJo.thumbnail
        }
        if (!url.startsWith("http"))
            url = "http://" + url

        BindingUtils.loadMediaImage(pictureImage, url)
        itemView.setOnClickListener {
            if (videoImageView.visibility == View.VISIBLE)
                IntentHelper.startVideoActivity(context, poJo.url)
        }

    }

}