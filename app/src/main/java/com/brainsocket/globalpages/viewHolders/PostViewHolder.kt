package com.brainsocket.globalpages.viewHolders

import android.graphics.drawable.RippleDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-06-29.
 */
class PostViewHolder : RecyclerView.ViewHolder {

    @BindView(R.id.post_Title)
    lateinit var postTitle: TextView

    @BindView(R.id.post_Image)
    lateinit var postImage: ImageView

    @BindView(R.id.post_Details_Container)
    lateinit var postDetailsContainer: View

    @BindView(R.id.post_details)
    lateinit var postDetails: TextView

    @BindView(R.id.post_Tag)
    lateinit var postTag: TextView


    constructor(view: View) : super(view) {
        ButterKnife.bind(this, view)
    }

    fun bind(post: Post) {
        postTitle.setText(post.title)
        postDetails.setText(post.description)
        var catTitle = (post.category.getTitle())
        postTag.text = catTitle
        BindingUtils.loadPostImage(postImage, postDetailsContainer, post)
    }


}