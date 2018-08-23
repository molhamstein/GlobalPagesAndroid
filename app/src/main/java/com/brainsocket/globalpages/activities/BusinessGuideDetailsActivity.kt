package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.MediaViewPagerAdapter
import com.brainsocket.globalpages.dialogs.ContactDialog
import com.brainsocket.globalpages.repositories.DummyDataRepositories
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator

class BusinessGuideDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.mediaViewPager)
    lateinit var mediaViewPager: ViewPager

    @BindView(R.id.viewPagerIndicator)
    lateinit var viewPagerIndicator: CircleIndicator

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_details_layout)
        ButterKnife.bind(this)
        mediaViewPager.adapter = MediaViewPagerAdapter(this, DummyDataRepositories.getMediaList())
        viewPagerIndicator.setViewPager(mediaViewPager)

        appbar.addOnOffsetChangedListener(this)

        initToolBar()

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_business_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

        }
        return true
    }

    @Optional
    @OnClick(R.id.contactContainer, R.id.contactBtn)
    fun onContactContainerClick(view: View) {
        val contactDialog = ContactDialog.newInstance()
        contactDialog.show(supportFragmentManager, ContactDialog.ContactDialog_Tag)
        Log.v("View Clicked", view.id.toString())
    }


}