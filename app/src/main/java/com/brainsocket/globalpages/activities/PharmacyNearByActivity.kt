package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.repositories.DummydataRepositories
import com.brainsocket.globalpages.views.TagSearchView
import com.brainsocket.mainlibrary.SupportViews.RecyclerViewDecoration.GridDividerDecoration

/**
 * Created by Adhamkh on 2018-07-18.
 */
class PharmacyNearByActivity : BaseActivity() {

    @BindView(R.id.businessGuideRecyclerView)
    lateinit var businessGuideRecyclerView: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.tagSearchView)
    lateinit var tagSearchView: TagSearchView

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun initRecyclerView() {
        businessGuideRecyclerView.layoutManager = LinearLayoutManager(this)
        businessGuideRecyclerView.addItemDecoration(GridDividerDecoration(this))
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.pharmcay_nearby_layout)
        ButterKnife.bind(this)

        initToolBar()
        initRecyclerView()

        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this
                , DummydataRepositories.getBusinessGuideList())

    }

    @OnClick(R.id.tagSearchView)
    fun onTagSearchViewClick(view: View) {
        Log.v("", "")
    }

}
