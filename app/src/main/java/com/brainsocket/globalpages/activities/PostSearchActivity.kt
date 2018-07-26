package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CityRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.LocationEntityRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.globalpages.listeners.OnCitySelectListener
import com.brainsocket.globalpages.repositories.DummydataRepositories


class PostSearchActivity : BaseActivity(), OnCategorySelectListener, OnCitySelectListener {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.filter_categories)
    lateinit var filterCategories: RecyclerView

    @BindView(R.id.filter_subCategories)
    lateinit var filterSubCategories: RecyclerView

    @BindView(R.id.filter_cities)
    lateinit var filterCities: RecyclerView

    @BindView(R.id.filter_locations)
    lateinit var filterLocations: RecyclerView


    private fun initRecyclerViews() {
        filterCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterCategories.adapter = CategoryRecyclerViewAdapter(this, DummydataRepositories.getCategoiesList(), this)

        filterSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummydataRepositories.getSubCategoriesList())

        filterCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterCities.adapter = CityRecyclerViewAdapter(this, DummydataRepositories.getCityList(), this)

        filterLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterLocations.adapter = LocationEntityRecyclerViewAdapter(this, DummydataRepositories.getLocationList())

    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_search_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()
    }

    override fun onSelectCategory(category: Category) {
        filterSubCategories.visibility = View.GONE
        Handler().postDelayed(object : Runnable {
            override fun run() {
                filterSubCategories.visibility = View.VISIBLE
            }
        }, 500)
    }

    override fun onSelectCity(city: City) {
        filterLocations.visibility = View.GONE
        Handler().postDelayed(object : Runnable {
            override fun run() {
                filterLocations.visibility = View.VISIBLE
            }
        }, 500)
    }

}