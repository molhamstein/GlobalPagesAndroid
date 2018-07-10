package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R

/**
 * Created by Adhamkh on 2018-06-08.
 */
class ProfileActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.flexible_example_fab)
    lateinit var mFab: View

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val PERCENTAGE_TO_SHOW_IMAGE = 20

    private var mMaxScrollSize: Int = 0
    private var mIsImageHidden: Boolean = false

    fun initToolBar() {
        setSupportActionBar(toolbar)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_layout)
        ButterKnife.bind(this)
        initToolBar()

        appbar.addOnOffsetChangedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {

            }
        }
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        var i = verticalOffset
        if (appBarLayout == null)
            return

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout!!.getTotalScrollRange()

        val currentScrollPercentage = Math.abs(i) * 100 / mMaxScrollSize

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true

                ViewCompat.animate(mFab).scaleY(0f).scaleX(0f).start()
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false
                ViewCompat.animate(mFab).scaleY(1f).scaleX(1f).start()
            }
        }
    }

}