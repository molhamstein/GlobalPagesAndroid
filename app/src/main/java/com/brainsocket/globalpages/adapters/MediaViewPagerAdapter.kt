package com.brainsocket.globalpages.adapters

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.MediaEntity
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-07-05.
 */
class MediaViewPagerAdapter constructor(var context: Context, var mediaList: MutableList<MediaEntity>) : PagerAdapter() {
    var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pojo = mediaList[position]
        var view = inflater.inflate(R.layout.media_item_layout, container, false)
        var mediaView: ImageView = view.findViewById(R.id.imageView)
        BindingUtils.loadMediaImage(mediaView, pojo)

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