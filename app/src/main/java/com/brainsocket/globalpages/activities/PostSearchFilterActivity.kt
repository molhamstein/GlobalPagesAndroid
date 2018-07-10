package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CityRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.LocationEntityRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.brainsocket.globalpages.repositories.DummydataRepositories

/**
 * Created by Adhamkh on 2018-06-29.
 */
class PostSearchFilterActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.filter_categories)
    lateinit var filter_categories: RecyclerView

    @BindView(R.id.filter_subCategories)
    lateinit var filter_subCategories: RecyclerView

    @BindView(R.id.filter_cities)
    lateinit var filter_cities: RecyclerView

    @BindView(R.id.filter_locations)
    lateinit var filter_locations: RecyclerView


    fun initRecyclerViews() {
        filter_categories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filter_categories.adapter = CategoryRecyclerViewAdapter(this, DummydataRepositories.getCategoiesList())

        filter_subCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filter_subCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummydataRepositories.getSubCategoriesList())

        filter_cities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filter_cities.adapter = CityRecyclerViewAdapter(this, DummydataRepositories.getCityList())

        filter_locations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filter_locations.adapter = LocationEntityRecyclerViewAdapter(this, DummydataRepositories.getLocationList())

    }

    fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.search_filter_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()

    }

}