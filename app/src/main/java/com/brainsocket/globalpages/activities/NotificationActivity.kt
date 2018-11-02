package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.NotificationRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.NotificationEntity
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.di.component.DaggerNotificationComponent
import com.brainsocket.globalpages.di.module.NotificationModule
import com.brainsocket.globalpages.di.ui.NotificationContract
import com.brainsocket.globalpages.di.ui.NotificationPresenter
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.SupportViews.RecyclerViewDecoration.GridDividerDecoration
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject


class NotificationActivity : BaseActivity(), NotificationContract.View {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var presenter: NotificationPresenter


    private fun initToolBar() {
        toolbar.setTitle(R.string.Notifications)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(GridDividerDecoration(this))
    }

    fun initDI() {
        val component = DaggerNotificationComponent.builder()
                .notificationModule(NotificationModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
        val user: User = UserRepository(this).getUser()!!
        presenter.loadNotifications(userId = user.id!!)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.notification_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerView()
        initDI()

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                val user: User = UserRepository(this@NotificationActivity).getUser()!!
                presenter.loadNotifications(userId = user.id!!)
            }

            override fun onRequestPermission() {

            }
        })
    }

    /*Notification presenter started*/
    override fun showNotificationProgress(show: Boolean) {
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showNotificationLoadErrorMessage(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showNotificationEmptyView(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onSeenNotificationsLoaded(notificationList: MutableList<NotificationEntity>) {
        recyclerView.adapter = NotificationRecyclerViewAdapter(this, notificationList)
    }
    /*Notification presenter ended*/
}