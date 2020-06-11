package com.almersal.android.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.almersal.android.R
import com.almersal.android.data.entities.MediaEntity
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper

class MediaViewPagerAdapter constructor(
    var context: Context,
    private var mediaList: MutableList<MediaEntity>
) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        if (mediaList.size == 0) mediaList = mutableListOf(MediaEntity("", "image", ""))
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val poJo = mediaList[position]
        val view = inflater.inflate(R.layout.media_item_layout, container, false)
        val mediaView: ImageView = view.findViewById(R.id.imageView)
        val videoImageView: ImageView = view.findViewById(R.id.videoImageView)

        if (poJo.url.endsWith("mp4", true))
            videoImageView.visibility = View.VISIBLE
        else
            videoImageView.visibility = View.GONE
        BindingUtils.loadMediaImage(mediaView, poJo, "Product")
        view.setOnClickListener {
            if (videoImageView.visibility == View.VISIBLE)
                IntentHelper.startVideoActivity(context, poJo.url)
            else {
                IntentHelper.startPictureListActivity(context, mediaList)
//                IntentHelper.startPictureActivity(context, poJo.url)
            }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == (`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mediaList.size
    }

}