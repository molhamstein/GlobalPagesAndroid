package com.brainsocket.globalpages.activities

import android.os.Bundle
import butterknife.ButterKnife
import com.brainsocket.globalpages.R

/**
 * Created by Adhamkh on 2018-10-10.
 */
class ProductAddActivity : BaseActivity() {
    companion object {
        const val ProductAddActivity_Tag = "ProductAddActivity_Tag"

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.product_add_layout)
        ButterKnife.bind(this)
    }
}