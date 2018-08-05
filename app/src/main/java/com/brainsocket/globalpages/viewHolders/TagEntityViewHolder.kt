package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.TagEntity

/**
 * Created by Adhamkh on 2018-07-04.
 */
class TagEntityViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.tag_toggle)
    lateinit var tag_toggle: ToggleButton

    @BindView(R.id.tag_close)
    lateinit var tag_close: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(tag: TagEntity) {
        tag_toggle.textOff = tag.getTitle()
        tag_toggle.textOn = tag.getTitle()
        tag_toggle.text = tag.getTitle()
    }

}