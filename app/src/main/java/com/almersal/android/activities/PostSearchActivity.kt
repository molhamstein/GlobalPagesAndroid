package com.almersal.android.activities

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
import com.almersal.android.R
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.di.component.DaggerPostSearchComponent
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.enums.FilterType
import com.almersal.android.enums.TagType
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnCitySelectListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.post_search_layout.*
import javax.inject.Inject


class PostSearchActivity : BaseActivity(), OnCategorySelectListener, OnCitySelectListener, TagsCollectionContact.View {

    companion object {
        const val Suggestion_List_Tag = "Suggestion_List"
        const val Post_Search_Filter_Type_Tag = "Post_Search_Filter_Type_Tag"
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

    @Inject
    lateinit var presenter: TagsCollectionPresenter

    var tags: MutableList<TagEntity>? = null

    private fun initRecyclerViews() {
        filterCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        filterLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
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
        when {
            filterType() == FilterType.ProductFilter -> {
                presenter.loadProductCategories()

                filterCities.visibility = View.GONE
                cityHeader.visibility = View.GONE
                cityLine.visibility = View.GONE



            }
            filterType() == FilterType.PostFilter ->
                presenter.loadPostCategories(true)
            filterType() == FilterType.BusinessFilter ->
                presenter.loadBusinessCategories(true)
            else -> presenter.getJobCategories(true)
        }
        presenter.loadCities(true)
    }

    private fun filterType(): FilterType {
        val res = intent.getIntExtra("Post_Search_Filter_Type_Tag", FilterType.PostFilter.type)
        if (res == FilterType.PostFilter.type) {
            return FilterType.PostFilter
        }
        if (res == FilterType.JobsFilter.type)
            return FilterType.JobsFilter

        if (res == FilterType.ProductFilter.type)
            return FilterType.ProductFilter
        return FilterType.BusinessFilter
    }


    /*Filter data started*/
    private fun initQuery() {
        tags?.forEach {
            when (it.tagType) {
                TagType.Query -> {
                    filter_searchKeyword.setText(it.titleEn)
                }
            }
        }
    }

    private fun initCategory(mutableList: MutableList<Category>): Pair<MutableList<Category>, Int> {
        var pos = -1
        tags?.forEach {
            when (it.tagType) {
                TagType.Category -> {
                    for (i in 0..(mutableList.size - 1)) {
                        val poJo = mutableList[i]
                        if (it.tagId == poJo.id) {
                            poJo.isSelected = true
                            pos = i
                            onSelectCategory(poJo)
                            break
                        }
                    }
                }
            }
        }
        return Pair(mutableList, pos)
    }

    private fun initSubCategory(mutableList: MutableList<SubCategory>): Pair<MutableList<SubCategory>, Int> {
        var pos = -1
        tags?.forEach {
            when (it.tagType) {
                TagType.SubCategory -> {
                    for (i in 0..(mutableList.size - 1)) {
                        val poJo = mutableList[i]
                        if (it.tagId == poJo.id) {
                            poJo.isSelected = true
                            pos = i
                            break
                        }
                    }
                }
            }
        }
        return Pair(mutableList, pos)
    }

    private fun initCity(mutableList: MutableList<City>): Pair<MutableList<City>, Int> {
        var pos = -1
        tags?.forEach {
            when (it.tagType) {
                TagType.City -> {
                    for (i in 0 until mutableList.size) {
                        val poJo = mutableList[i]
                        if (it.tagId == poJo.id) {
                            poJo.isSelected = true
                            pos = i
                            onSelectCity(poJo)
                            break
                        }
                    }
                }
            }
        }
        return Pair(mutableList, pos)
    }

    private fun initLocation(mutableList: MutableList<LocationEntity>): Pair<MutableList<LocationEntity>, Int> {
        var pos = -1
        tags?.forEach {
            when (it.tagType) {
                TagType.Location -> {
                    for (i in 0..(mutableList.size - 1)) {
                        val poJo = mutableList[i]
                        if (it.tagId == poJo.id) {
                            poJo.isSelected = true
                            pos = i
                            break
                        }
                    }
                }
            }
        }
        return Pair(mutableList, pos)
    }
    /*Filter data ended*/

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_search_layout)
        ButterKnife.bind(this)
        tags = Gson().fromJson(
            intent.extras.getString(Suggestion_List_Tag).toString(),
            Array<TagEntity>::class.java
        ).toMutableList()

        initToolBar()
        initRecyclerViews()
        initDI()

        initQuery()

    }

    @OnClick(R.id.filterBtn)
    fun onFilterButtonClick(view: View) {

        val filterEntity = FilterEntity()
        filterEntity.query = filter_searchKeyword.text.toString()

        if (filterCategories.adapter != null) {
            val category = (filterCategories.adapter as CategoryRecyclerViewAdapter).getCurrentCategory()
            if (category != null)
                filterEntity.category = category
        }
        if (filterSubCategories.adapter != null)
            filterEntity.subCategory =
                (filterSubCategories.adapter as SubCategoryRecyclerViewAdapter).getCurrentSubCategory()

        if (filterCities.adapter != null)
            filterEntity.city = (filterCities.adapter as CityRecyclerViewAdapter).getCurrentCity()
        if (filterLocations.adapter != null)
            filterEntity.area = (filterLocations.adapter as LocationEntityRecyclerViewAdapter).getCurrentLocation()

        if (filterType() == FilterType.PostFilter || filterType() == FilterType.ProductFilter) {
            RxBus.publish(MessageEvent(EventActions.Post_Filter_Activity_Tag, filterEntity))
        } else if (filterType() == FilterType.BusinessFilter) {
            RxBus.publish(MessageEvent(EventActions.Business_Filter_Activity_Tag, filterEntity))
        } else {
            RxBus.publish(MessageEvent(EventActions.Job_Filter_Activity_Tag, filterEntity))
        }

        finish()
    }


    /*Post Categories started*/

    override fun showPostCategoriesProgress(show: Boolean) {
        super.showPostCategoriesProgress(show)
    }

    override fun showPostCategoriesLoadErrorMessage(visible: Boolean) {
        super.showPostCategoriesLoadErrorMessage(visible)
    }

    override fun showPostCategoriesEmptyView(visible: Boolean) {
        super.showPostCategoriesEmptyView(visible)
    }

    override fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {
        val res = initCategory(categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList())
        filterCategories.adapter =
            CategoryRecyclerViewAdapter(context = this, categoriesList = res.first, onCategorySelectListener = this)
        if (res.second > 0)
            filterCategories.smoothScrollToPosition(res.second)
    }

    override fun showBusinessCategoriesProgress(show: Boolean) {
        super.showPostCategoriesProgress(show)
    }

    override fun showBusinessCategoriesLoadErrorMessage(visible: Boolean) {
        super.showPostCategoriesLoadErrorMessage(visible)
    }

    override fun showBusinessCategoriesEmptyView(visible: Boolean) {
        super.showPostCategoriesEmptyView(visible)
    }

    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
        val res = initCategory(categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList())
        filterCategories.adapter = CategoryRecyclerViewAdapter(
            context = this, categoriesList = res.first,
            onCategorySelectListener = this
        )
        if (res.second > 0)
            filterCategories.smoothScrollToPosition(res.second)
    }

    /*Event not presenter started*/
    override fun onSelectCategory(category: Category) {
        subCategoryContainer.visibility = View.GONE

        subCategoryContainer.visibility = View.VISIBLE
        val subCategories = category.subCategoriesList
        val res = initSubCategory(subCategories)
        filterSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, res.first)
        if (res.second > 0)
            filterSubCategories.smoothScrollToPosition(res.second)
    }
    /*Event not presenter ended*/

    /*Post Categories ended*/


    /*Post Cities started*/

    override fun showCitiesProgress(show: Boolean) {
        super.showCitiesProgress(show)
    }

    override fun showCitiesLoadErrorMessage(visible: Boolean) {
        super.showCitiesLoadErrorMessage(visible)
    }

    override fun showCitiesEmptyView(visible: Boolean) {
        super.showCitiesEmptyView(visible)
    }

    override fun onCitiesLoaded(citiesList: MutableList<City>) {
        val res = initCity(citiesList)
        filterCities.adapter = CityRecyclerViewAdapter(this, res.first, this)
        if (res.second > 0)
            filterCities.smoothScrollToPosition(res.second)
    }

    /*Event not presenter started*/
    override fun onSelectCity(city: City) {
        areaContainer.visibility = View.GONE
        areaContainer.visibility = View.VISIBLE
        val res = initLocation(city.locations)
        filterLocations.adapter = LocationEntityRecyclerViewAdapter(this, res.first)
        if (res.second > 0)
            filterLocations.smoothScrollToPosition(res.second)
    }

    override fun showJobCategoriesLoadErrorMessage(visible: Boolean) {}
    override fun showJobCategoriesEmptyView(visible: Boolean) {}
    override fun showJobCategoriesProgress(visible: Boolean) {}
    override fun onJobCategoriesLoaded(categoriesList: MutableList<JobCategory>) {
        val res = initCategory(categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList())
        filterCategories.adapter = CategoryRecyclerViewAdapter(
            context = this, categoriesList = res.first,
            onCategorySelectListener = this
        )
        if (res.second > 0)
            filterCategories.smoothScrollToPosition(res.second)

    }
    /*Event not presenter ended*/

    /*Post Cities started*/


}