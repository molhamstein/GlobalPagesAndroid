package com.almersal.android.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.utilities.BindingUtils


class PictureActivity : BaseActivity() {

    companion object {
        const val PictureActivity_Tag = "PictureActivity_Tag"
    }

    @BindView(R.id.pictureImage)
    lateinit var pictureImage: ImageView

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.picture_layout)
        ButterKnife.bind(this)
        val url = intent.getStringExtra(PictureActivity_Tag)
        BindingUtils.loadImage(pictureImage, url)
    }

    @OnClick(R.id.closeBtn)
    fun onCloseButtonClick(view: View) {
        finish()
    }

}