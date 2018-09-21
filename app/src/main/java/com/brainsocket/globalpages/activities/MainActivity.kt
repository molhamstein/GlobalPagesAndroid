package com.brainsocket.globalpages.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideSliderRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.TagsRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.data.entities.Volume
import com.brainsocket.globalpages.data.filtration.FilterEntity
import com.brainsocket.globalpages.di.component.DaggerVolumesComponent
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.ui.VolumesContract
import com.brainsocket.globalpages.di.ui.VolumesPresenter
import com.brainsocket.globalpages.listeners.OnTagSelectListener
import com.brainsocket.globalpages.repositories.DummyDataRepositories
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.globalpages.views.SelectedTagsView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.NotificationBadge
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.google.gson.Gson
import javax.inject.Inject


class MainActivity : BaseActivity(), VolumesContract.View, OnTagSelectListener {

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

    @BindView(R.id.volumeTitle)
    lateinit var volumeTitle: TextView


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
        presenter.loadDefaultVolume()
    }

    private fun initBusinessGuideRecyclerView() {
        val snapHelper = LinearSnapHelper()
        businessGuideRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(businessGuideRecyclerView)
        businessGuideRecyclerView.adapter = BusinessGuideSliderRecyclerViewAdapter(this, DummyDataRepositories.getBusinessGuideList())
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

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummyDataRepositories.getTagsDefaultRepositories()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this

//        tagSearch.addSuggestionList(DummydataRepositories.getTagsRepositories())
//        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, DummyDataRepositories.getPostList())
//        intentHelper.startPostAddActivity(this)
//        intentHelper.startBusinessAddActivity(this)
//        intentHelper.startBusinessGuideDetailsActivity(this)
//        intentHelper.startBusinessGuideSearchActivity(this)

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                presenter.loadDefaultVolume()
            }

            override fun onRequestPermission() {

            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            PostSearchActivity.PostSearchActivity_ResultCode -> {
                val filter = data!!.extras[PostSearchActivity.PostSearchActivity_Filter_Tag].toString()
                val filterEntity = Gson().fromJson(filter, FilterEntity::class.java)
                selectedTagsView.setAdapter(TagsRecyclerViewAdapter(baseContext, filterEntity.getTags()))
                (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this
                if (volumesRecyclerView.adapter != null) {
                    (volumesRecyclerView.adapter as PostRecyclerViewAdapter).filterByCreteria(filterEntity)
                }
                Log.v("", "")
            }
        }

    }

    override fun onSelectTag(tagEntity: TagEntity) {
        (volumesRecyclerView.adapter as PostRecyclerViewAdapter).excludeFilter(tagEntity)
        Log.v("", "")
    }

    @OnClick(R.id.previousBtn)
    fun onPreviousButtonClick(view: View) {
        presenter.loadPreviousVolume()
    }

    @OnClick(R.id.nextBtn)
    fun onNextButtonClick(view: View) {
        presenter.loadNextVolume()
    }

    @OnClick(R.id.searchFilterBtn)
    fun onSearchFilterBtnClick(view: View) {
        var list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(MainActivity@ this, list)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.loginBtn)
    fun onLoginBtnClick(view: View) {
        val usr = UserRepository(this).getUser()
        if (usr != null)
            IntentHelper.startProfileActivity(this)
        else
            IntentHelper.startSignInActivity(MainActivity@ this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.businessGuideBtn)
    fun onBusinessGuideClick(view: View) {
        IntentHelper.startBusinessGuideSearchActivity(this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.findNearByBtn)
    fun onFindNearByClick(view: View) {
        val activityName = PharmacyNearByActivity::class.java.canonicalName
        IntentHelper.startLocationCheckActivity(this, activityName)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.addBusinessBtn)
    fun onAddBusinessBtnClick(view: View) {
        val user = UserRepository(this).getUser()
        if (user != null)
            IntentHelper.startBusinessAddActivity(this)
        else
            IntentHelper.startSignInActivity(this)
    }

    @OnClick(R.id.dutyPharmacyBtn)
    fun onDutyPharmacyClick(view: View) {
        IntentHelper.startDutyPharmacyActivity(this)
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
        volumeTitle.text = volume.getTitle()
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, volume.posts)
    }

    override fun noMoreData() {
        Toast.makeText(this, R.string.noMoreVolumeFound, Toast.LENGTH_LONG).show()
    }
    /*Presenter ended*/

}