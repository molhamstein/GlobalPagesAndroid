package com.brainsocket.globalpages.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-06-28.
 */
class PostSliderViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.postTitle)
    lateinit var postTitle: TextView

    @BindView(R.id.postIcon)
    lateinit var postIcon: ImageView

    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(post: Post) {
        postTitle.text = post.title
        BindingUtils.loadPostSliderGuideImage(postIcon, post)
    }

}