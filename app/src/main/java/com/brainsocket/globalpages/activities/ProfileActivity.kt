package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.repositories.DummydataRepositories
import com.brainsocket.globalpages.views.CustomTabView


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

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

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
        myPosts.adapter = PostRecyclerViewAdapter(this, DummydataRepositories.getPostList(), true)

        myBusiness.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        myBusiness.adapter = BusinessGuideRecyclerViewAdapter(this, DummydataRepositories.getBusinessGuideList())
    }

    private fun initTabLayout() {
        val maleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.male, R.mipmap.ic_male_24dp)).setText(R.string.male)
        val femaleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.female, R.mipmap.ic_female_24dp)).setText(R.string.female)

        genderTabLayout.addTab(maleTab)
        genderTabLayout.addTab(femaleTab)

        genderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.v("", "")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.grayLightTextColor))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            }
        })

        femaleTab.select()
        maleTab.select()

    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()
        initTabLayout()
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