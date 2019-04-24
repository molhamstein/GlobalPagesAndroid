package com.almersal.android.activities

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.PostRecyclerViewAdapter
import com.almersal.android.data.entities.Volume
import com.almersal.android.di.component.DaggerVolumesComponent
import com.almersal.android.di.module.VolumesModule
import com.almersal.android.di.ui.VolumesContract
import com.almersal.android.di.ui.VolumesPresenter
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject


class VolumesActivity : BaseActivity(), VolumesContract.View {

    companion object {
        const val VolumesActivity_Tag = "VolumesActivity_Tag"
    }

    @Inject
    lateinit var presenter: VolumesPresenter

    @BindView(R.id.statesLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar


    private fun initDI() {
        val component = DaggerVolumesComponent.builder()
                .volumesModule(VolumesModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadVolumeById(intent.getStringExtra(VolumesActivity_Tag))

    }

    private fun initToolBar() {
        toolBar.setTitle(R.string.app_name)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.volumes_layout)
        ButterKnife.bind(this)
        initToolBar()
        initDI()

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                presenter.loadVolumeById(intent.getStringExtra(VolumesActivity_Tag))
            }

            override fun onRequestPermission() {

            }
        })

    }


    /*Presenter volumes started*/

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
        toolBar.title = volume.getTitle()
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, volume.posts)
    }

    override fun noMoreData() {

    }

    override fun disableNext() {

    }

    override fun enableNext() {

    }

    override fun disablePrev() {

    }

    override fun enablePrev() {

    }
    /*Presenter volumes ended*/
}