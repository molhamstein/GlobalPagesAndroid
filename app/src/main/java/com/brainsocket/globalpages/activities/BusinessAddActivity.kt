package com.brainsocket.globalpages.activities

import android.os.Bundle
import butterknife.ButterKnife
import com.brainsocket.globalpages.R

/**
 * Created by Adhamkh on 2018-06-08.
 */
class BusinessAddActivity : BaseActivity() {

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_add_layout)
        ButterKnife.bind(this)

    }

}