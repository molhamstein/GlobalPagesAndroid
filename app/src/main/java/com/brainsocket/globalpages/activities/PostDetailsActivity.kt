package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.MediaViewPagerAdapter
import com.brainsocket.globalpages.repositories.DummydataRepositories
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator


class PostDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        val PostId_Tag = "PostId"
    }

    @BindView(R.id.mediaViewPager)
    lateinit var mediaViewPager: ViewPager

    @BindView(R.id.viewPagerIndicator)
    lateinit var viewPagerIndicator: CircleIndicator

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_details_layout)
        ButterKnife.bind(this)
        mediaViewPager.adapter = MediaViewPagerAdapter(this, DummydataRepositories.getMediaList())
        viewPagerIndicator.setViewPager(mediaViewPager)

        appbar.addOnOffsetChangedListener(this)

        initToolBar()

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

}