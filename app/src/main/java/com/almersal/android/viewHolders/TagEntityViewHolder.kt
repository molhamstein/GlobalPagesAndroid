package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.TagEntity

/**
 * Created by Adhamkh on 2018-07-04.
 */
class TagEntityViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.tag_toggle)
    lateinit var tag_toggle: ToggleButton

    @BindView(R.id.tag_close)
    lateinit var tag_close: ImageView

    @BindView(R.id.tagName)
    lateinit var tagName: TextView

    init {
        ButterKnife.bind(this, view)
    }

    fun bind(tag: TagEntity) {
        tag_toggle.textOff = tag.getTitle()
        tag_toggle.textOn = tag.getTitle()
        tag_toggle.text = tag.getTitle()
        tagName.text = tag.getTitle()
    }

}