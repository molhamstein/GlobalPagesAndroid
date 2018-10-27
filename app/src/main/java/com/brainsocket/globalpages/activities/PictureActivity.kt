package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.utilities.BindingUtils

/**
 * Created by Adhamkh on 2018-10-27.
 */
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