package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideSliderRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.TagsRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Volume
import com.brainsocket.globalpages.di.component.DaggerVolumesComponent
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.ui.VolumesContract
import com.brainsocket.globalpages.di.ui.VolumesPresenter
import com.brainsocket.globalpages.repositories.DummydataRepositories
import com.brainsocket.globalpages.repositories.userRepository
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.views.SelectedTagsView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.NotificationBadge
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject


class MainActivity : BaseActivity(), VolumesContract.View {

    @Inject
    lateinit var presenter: VolumesPresenter

    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar

    @BindView(R.id.businessGuideRecyclerView)
    lateinit var businessGuideRecyclerView: RecyclerView

    @BindView(R.id.volumesRecyclerView)
    lateinit var volumesRecyclerView: RecyclerView

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.selectedTagsView)
    lateinit var selectedTagsView: SelectedTagsView

    @BindView(R.id.badge)
    lateinit var badge: NotificationBadge

    private fun initToolBar() {
        toolBar.setTitle(R.string.app_name)
        setSupportActionBar(toolBar)
    }

    private fun initDI() {
        val component = DaggerVolumesComponent.builder()
                .volumesModule(VolumesModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        // presenter.loadDefaultVolume()
    }

    private fun initBusinessGuideRecyclerView() {
        val snapHelper = LinearSnapHelper()
        businessGuideRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(businessGuideRecyclerView)
        businessGuideRecyclerView.adapter = BusinessGuideSliderRecyclerViewAdapter(this, DummydataRepositories.getBusinessGuideList())
    }

    private fun initVolumesRecyclerView() {
        volumesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.main_layout)
        ButterKnife.bind(this)
        initToolBar()
        initBusinessGuideRecyclerView()
        initVolumesRecyclerView()
        initDI()

        badge.setNumber(6, true)

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummydataRepositories.getTagsDefaultRepositories()))
//        tagSearch.addSuggestionList(DummydataRepositories.getTagsRepositories())
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, DummydataRepositories.getPostList())

//        intentHelper.startPostAddActivity(this)
//        intentHelper.startBusinessAddActivity(this)
//        intentHelper.startBusinessGuideDetailsActivity(this)

//        intentHelper.startBusinessGuideSearchActivity(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        if (tagSearch.isExpand()) {
//            tagSearch.setExpand(false)
//            mainHelper.hideKeyboard(tagSearch)
//        } else {
//            super.onBackPressed()
//        }
    }

    @OnClick(R.id.searchFilterBtn)
    fun onSearchFilterBtnClick(view: View) {
        var list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        intentHelper.startPostSearchFilterActivity(MainActivity@ this, list)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.loginBtn)
    fun onLoginBtnClick(view: View) {
        val usr = userRepository(this).getUser()
        if (usr != null)
            intentHelper.startProfileActivity(this)
        else
            intentHelper.startSignInActivity(MainActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.businessGuideBtn)
    fun onBusinessGuideClick(view: View) {
        intentHelper.startBusinessGuideSearchActivity(this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.findNearByBtn)
    fun onFindNearByClick(view: View) {
        val activityName = PharmacyNearByActivity::class.java.canonicalName
        intentHelper.startLocationCheckActivity(this, activityName)
//        intentHelper.startNearByPharmaciesActivity(this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.addBusinessBtn)
    fun onAddBusinessBtnClick(view: View) {
        var user = userRepository(this).getUser()
        if (user != null)
            intentHelper.startBusinessAddActivity(this)
        else
            intentHelper.startSignInActivity(this)
    }

    @OnClick(R.id.dutyPharmacyBtn)
    fun onDutyPharmacyClick(view: View) {
        intentHelper.startDutyPharmacyActivity(this)
        Log.v("View Clicked", view.id.toString())
    }


    /*Presenter started*/
    override fun showProgress(show: Boolean) {
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showEmptyView(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun loadedData(volume: Volume) {
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, volume.posts)
    }

    override fun noMoreData() {

    }
    /*Presenter ended*/
}