package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.*
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Volume
import com.brainsocket.globalpages.di.component.DaggerVolumesComponent
import com.brainsocket.globalpages.di.module.VolumesModule
import com.brainsocket.globalpages.di.ui.VolumesContract
import com.brainsocket.globalpages.di.ui.VolumesPresenter
import com.brainsocket.globalpages.repositories.DummydataRepositories
import com.brainsocket.globalpages.repositories.userRepository
import com.brainsocket.globalpages.utilities.intentHelper
import com.brainsocket.globalpages.utilities.mainHelper
import com.brainsocket.globalpages.views.TagSearchView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject

/**
 * Created by Adhamkh on 2018-06-08.
 */
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

    @BindView(R.id.tagSearch)
    lateinit var tagSearch: TagSearchView

    fun initToolBar() {
        toolBar.setTitle(R.string.app_name)
        setSupportActionBar(toolBar)
    }

    fun initDI() {
        val component = DaggerVolumesComponent.builder()
                .volumesModule(VolumesModule(this))
                .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()
        // presenter.loadDefaultVolume()
    }

    fun initBusinessGuideRecyclerView() {
        var snapHelper = LinearSnapHelper()
        businessGuideRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(businessGuideRecyclerView)
        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this)
    }

    fun initVolumesRecyclerView() {
        volumesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.main_layout)
        ButterKnife.bind(this)
        initToolBar()
        initBusinessGuideRecyclerView()
        initVolumesRecyclerView()
        initDI()

//        intentHelper.startBusinessAddActivity(context = this)
        tagSearch.addSuggestionList(DummydataRepositories.getTagsRepositories())
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, DummydataRepositories.getPostList())
    }

    override fun onBackPressed() {
        if (tagSearch.isExpand()) {
            tagSearch.setExpand(false)
            mainHelper.hideKeyboard(tagSearch)
        } else {
            super.onBackPressed()
        }
    }

    @OnClick(R.id.searchFilterBtn)
    fun onSearchFilterBtnClick(view: View) {
        intentHelper.startPostSearchFilterActivity(MainActivity@ this)
    }

    @OnClick(R.id.loginBtn)
    fun onLoginBtnClick(view: View) {
        var usr = userRepository(this).getUser()
        if (usr != null)
            intentHelper.startProfileActivity(this)
        else
            intentHelper.startSignInActivity(MainActivity@ this)
    }

    /*Presenter started*/
    override fun showProgress(show: Boolean) {
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showLoadErrorMessage(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showEmptyView(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun loadedData(volume: Volume) {
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, volume.posts)
        Log.v("", "")
    }

    override fun noMoreData() {

    }
    /*Presenter ended*/
}