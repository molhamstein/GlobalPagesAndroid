package com.brainsocket.globalpages.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.*
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.data.entities.City
import com.brainsocket.globalpages.data.entities.PostCategory
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.data.filtration.FilterEntity
import com.brainsocket.globalpages.di.component.DaggerPostSearchComponent
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.ui.TagsCollectionContact
import com.brainsocket.globalpages.di.ui.TagsCollectionPresenter
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.globalpages.listeners.OnCitySelectListener
import com.brainsocket.globalpages.repositories.DummyDataRepositories
import com.google.gson.Gson
import javax.inject.Inject


class PostSearchActivity : BaseActivity(), OnCategorySelectListener, OnCitySelectListener, TagsCollectionContact.View {

    @Inject
    lateinit var presenter: TagsCollectionPresenter

    companion object {
        const val Suggestion_List_Tag = "Suggestion_List"
        const val PostSearchActivity_Filter_Tag = "PostSearchActivity_Filter_Tag"
        const val PostSearchActivity_ResultCode = 100
    }

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

    @BindView(R.id.subCategoryContainer)
    lateinit var subCategoryContainer: LinearLayout

    @BindView(R.id.areaContainer)
    lateinit var areaContainer: LinearLayout


    @BindView(R.id.filter_searchKeyword)
    lateinit var filter_searchKeyword: EditText

    private fun initRecyclerViews() {
        filterCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        filterCategories.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList(), this)

        filterSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        filterSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummyDataRepositories.getSubCategoriesList())

        filterCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        filterCities.adapter = CityRecyclerViewAdapter(this, DummyDataRepositories.getCityList(), this)

        filterLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        filterLocations.adapter = LocationEntityRecyclerViewAdapter(this, DummyDataRepositories.getLocationList())

//        suggestionTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummydataRepositories.getTagsRepositories(), false))

    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initDI() {
        val component = DaggerPostSearchComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadPostCategories(true)
        presenter.loadCities(true)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_search_layout)
        ButterKnife.bind(this)
        val tags: MutableList<TagEntity> = Gson().fromJson(intent.extras.getString(Suggestion_List_Tag).toString(),
                Array<TagEntity>::class.java).toMutableList()
//        searchTagView.setSuggestionList(tags)

        initToolBar()
        initRecyclerViews()
        initDI()

    }

    @OnClick(R.id.filterBtn)
    fun OnFilterButtonClick(view: View) {

        var intent = Intent()
        var filterEntity = FilterEntity()
        filterEntity.query = filter_searchKeyword.text.toString()

        if (filterCategories.adapter != null) {
            var category = (filterCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (category != null)
                filterEntity.postCategory = category as PostCategory
        }
        if (filterSubCategories.adapter != null)
            filterEntity.subCategory = (filterSubCategories.adapter as SubCategoryRecyclerViewAdapter).getCurrentSubCategory()

        if (filterCities.adapter != null)
            filterEntity.city = (filterCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
        if (filterLocations.adapter != null)
            filterEntity.area = (filterLocations.adapter as LocationEntityRecyclerViewAdapter).getCurrentLocation()


        intent.putExtra(PostSearchActivity_Filter_Tag, Gson().toJson(filterEntity).toString())
        setResult(PostSearchActivity_ResultCode, intent)
        finish()
    }

    override fun onSelectCategory(category: Category) {
        subCategoryContainer.visibility = View.GONE
        Handler().postDelayed({
            subCategoryContainer.visibility = View.VISIBLE
            filterSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, category.subCategoriesList)
        }, 500)
    }

    override fun onSelectCity(city: City) {
        areaContainer.visibility = View.GONE
        Handler().postDelayed({
            areaContainer.visibility = View.VISIBLE
            filterLocations.adapter = LocationEntityRecyclerViewAdapter(this, city.locations)
        }, 500)
    }

    /*Post Categories started*/
    override fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {
        filterCategories.adapter = CategoryRecyclerViewAdapter(this, categoriesList.toMutableList(), this)
    }

    override fun showPostCategoriesProgress(show: Boolean) {
        super.showPostCategoriesProgress(show)
    }

    override fun showPostCategoriesLoadErrorMessage(visible: Boolean) {
        super.showPostCategoriesLoadErrorMessage(visible)
    }

    override fun showPostCategoriesEmptyView(visible: Boolean) {
        super.showPostCategoriesEmptyView(visible)
    }
    /*Post Categories started*/

    /*Post Cities started*/
    override fun onCitiesLoaded(citiesList: MutableList<City>) {
        super.onCitiesLoaded(citiesList)
        filterCities.adapter = CityRecyclerViewAdapter(this, citiesList, this)
    }

    override fun showCitiesProgress(show: Boolean) {
        super.showCitiesProgress(show)
    }

    override fun showCitiesLoadErrorMessage(visible: Boolean) {
        super.showCitiesLoadErrorMessage(visible)
    }

    override fun showCitiesEmptyView(visible: Boolean) {
        super.showCitiesEmptyView(visible)
    }
    /*Post Cities started*/


}