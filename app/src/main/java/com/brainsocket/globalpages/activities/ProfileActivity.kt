package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.repositories.DummydataRepositories


class ProfileActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.flexible_example_fab)
    lateinit var mFab: View

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.myCategories)
    lateinit var myCategories: RecyclerView

    @BindView(R.id.myPosts)
    lateinit var myPosts: RecyclerView

    @BindView(R.id.myBusiness)
    lateinit var myBusiness: RecyclerView

    private val percentageToShowImage = 20

    private var mMaxScrollSize: Int = 0
    private var mIsImageHidden: Boolean = false

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerViews() {
        myCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        myCategories.adapter = CategoryRecyclerViewAdapter(this, DummydataRepositories.getCategoiesList())

        myPosts.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        myPosts.adapter = PostRecyclerViewAdapter(this, DummydataRepositories.getPostList())

        myBusiness.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        myBusiness.adapter = BusinessGuideRecyclerViewAdapter(this, DummydataRepositories.getBusinessGuideList())
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()
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
//        val i = verticalOffset
        if (appBarLayout == null)
            return

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / mMaxScrollSize

        if (currentScrollPercentage >= percentageToShowImage) {
            if (!mIsImageHidden) {
                mIsImageHidden = true

                ViewCompat.animate(mFab).scaleY(0f).scaleX(0f).start()
            }
        }

        if (currentScrollPercentage < percentageToShowImage) {
            if (mIsImageHidden) {
                mIsImageHidden = false
                ViewCompat.animate(mFab).scaleY(1f).scaleX(1f).start()
            }
        }
    }

}