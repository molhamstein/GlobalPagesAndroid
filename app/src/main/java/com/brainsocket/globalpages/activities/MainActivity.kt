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
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostSliderRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.TagsRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entities.TagEntity
import com.brainsocket.globalpages.data.entities.Volume
import com.brainsocket.globalpages.data.filtration.FilterEntity
import com.brainsocket.globalpages.di.component.DaggerMainComponent
import com.brainsocket.globalpages.di.module.PostModule
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.ui.PostContract
import com.brainsocket.globalpages.di.ui.PostPresenter
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


class MainActivity : BaseActivity(), VolumesContract.View, PostContract.View, OnTagSelectListener {

    @Inject
    lateinit var presenter: VolumesPresenter

    @Inject
    lateinit var postPresenter: PostPresenter


    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar

    @BindView(R.id.featuredPostsRecyclerView)
    lateinit var featuredPostsRecyclerView: RecyclerView

    @BindView(R.id.volumesRecyclerView)
    lateinit var volumesRecyclerView: RecyclerView

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.featuredPostStatesLayout)
    lateinit var featuredPostStatesLayout: Stateslayoutview

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
        val component = DaggerMainComponent.builder()
                .volumesModule(VolumesModule(this))
                .postModule(PostModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadDefaultVolume()


        postPresenter.attachView(this)
        postPresenter.subscribe()
        postPresenter.loadFeaturedPost()
    }

    private fun initBusinessGuideRecyclerView() {
        val snapHelper = LinearSnapHelper()
        featuredPostsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(featuredPostsRecyclerView)
//        businessGuideRecyclerView.adapter = BusinessGuideSliderRecyclerViewAdapter(this, DummyDataRepositories.getBusinessGuideList())
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

        featuredPostStatesLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                postPresenter.loadFeaturedPost()
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
                    (volumesRecyclerView.adapter as PostRecyclerViewAdapter).filterByCriteria(filterEntity)
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
        val activityName = BusinessGuideSearchActivity::class.java.canonicalName
        IntentHelper.startLocationCheckActivity(this, activityName)
//        IntentHelper.startBusinessGuideSearchActivity(this)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.findNearByBtn)
    fun onFindNearByClick(view: View) {
        val activityName = FindNearByActivity::class.java.canonicalName
        IntentHelper.startLocationCheckActivity(this, activityName)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.dutyPharmacyBtn)
    fun onDutyPharmacyClick(view: View) {
        val activityName = PharmacyDutySearchActivity::class.java.canonicalName
        IntentHelper.startLocationCheckActivity(this, activityName)
//        IntentHelper.startDutyPharmacyActivity(this)
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


    /*Post Presenter started*/
    override fun showFeaturedPostProgress(show: Boolean) {
        if (show) {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showFeaturedPostLoadErrorMessage(visible: Boolean) {
        if (visible) {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showFeaturedPostEmptyView(visible: Boolean) {
        if (visible) {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            featuredPostStatesLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onFeaturedPostLoadedSuccessfully(postList: MutableList<Post>) {
        featuredPostsRecyclerView.adapter = PostSliderRecyclerViewAdapter(context = baseContext, postList = postList)
    }
    /*Post Presenter ended*/


}