package com.almersal.android.viewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.Post
import com.almersal.android.utilities.BindingUtils


class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    init {
        ButterKnife.bind(this, view)
    }

    @BindView(R.id.post_Title)
    lateinit var postTitle: TextView

    @BindView(R.id.post_Image)
    lateinit var postImage: ImageView

    @BindView(R.id.post_city)
    lateinit var postCity: TextView

    @BindView(R.id.post_area)
    lateinit var postArea: TextView

    @BindView(R.id.post_details)
    lateinit var postDetails: TextView

    @BindView(R.id.post_Tag)
    lateinit var postTag: TextView


    fun bind(post: Post) {
        postTitle.text = post.title
        postDetails.text = post.description
        postTag.text = post.category.getTitle()
        postCity.text = post.city.getTitle()
        postArea.text = post.location.getTitle()

        BindingUtils.loadPostImage(postImage, post)
    }


}