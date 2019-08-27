package com.almersal.android.activities

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.adapters.NotificationRecyclerViewAdapter
import com.almersal.android.data.entities.NotificationEntity
import com.almersal.android.data.entities.User
import com.almersal.android.di.component.DaggerNotificationComponent
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.ui.NotificationContract
import com.almersal.android.di.ui.NotificationPresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.repositories.UserRepository
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

    var limit = 10
    var skip = 0

    private fun initToolBar() {
        toolbar.setTitle(R.string.Notifications)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(GridDividerDecoration(this))

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//
//                val lm = recyclerView.layoutManager as LinearLayoutManager
//                val visibleItemCount = lm.childCount
//                val totalItemCount = lm.itemCount
//                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
//                if (progressBar.visibility == View.GONE)
//                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                        && totalItemCount >= limit
//                    ) {
//                        pageId++
//                        businessGuidesPresenter.loadBusinessGuideByLocation(
//                            pointEntity =
//                            PointEntity(lat = lastLocation!!.latitude, lng = lastLocation!!.longitude),
//                            limit = limit,
//                            skip = limit * pageId
//                        )
//                    }
//            }
//        })

    }

    fun initDI() {
        val component = DaggerNotificationComponent.builder()
                .notificationModule(NotificationModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
        val user: User = UserRepository(this).getUser()!!
        presenter.loadNotifications(userId = user.id!!, limit = 10, skip = 0)
//        presenter.loadNotifications()
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
                presenter.loadNotifications(userId = user.id!!, limit = 10, skip = 0)
            }

            override fun onRequestPermission() {

            }
        })


        RxBus.listen(MessageEvent::class.java).subscribe {
            if (isFinishing)
                return@subscribe
            when (it.action) {

                EventActions.Notification_Delete_By_Id -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.delete_notification)
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        presenter.deleteNotificationById(it.message as String)
                        //alertDialog?.dismiss()
                    }
                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        //alertDialog?.dismiss()
                    }
                    builder.show()
                    //presenter.deleteNotificationById(it.message as String)
                }

                EventActions.Notification_Set_Clicked -> {
                    presenter.setNotificationClicked(it.message as NotificationEntity)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notification_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                val builder1 = AlertDialog.Builder(this)
                builder1.setMessage(R.string.doYouWantDeleteAllNotifications)
                builder1.setCancelable(true)

                builder1.setPositiveButton(
                        R.string.Yes,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                presenter.clearAllNotifications(context = this@NotificationActivity)
                            }
                        })

                builder1.setNegativeButton(
                        R.string.No,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {

                            }
                        })

                val alert11 = builder1.create()
                alert11.show()
            }
        }
        return true
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
        presenter.setNotificationSeen(notificationIds = notificationList.map { it.id }.toMutableList())
    }

    override fun onNotificationSetSeenSuccessfully() {
        super.onNotificationSetSeenSuccessfully()
    }

    override fun onNotificationSetClickedSuccessfully() {
        super.onNotificationSetClickedSuccessfully()
        val user: User = UserRepository(this).getUser()!!
        presenter.loadNotifications(userId = user.id!!, limit = 10, skip = 0)
    }

    override fun onNotificationSetSeenFailed() {
        super.onNotificationSetSeenFailed()
    }
    /*Notification presenter ended*/


    /*Notifications Delete presenter started*/
    override fun onNotificationsDeleteProgress(show: Boolean) {
        if (show) {
            val progressDialog = ProgressDialog.newInstance()
            progressDialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.closeDialog(supportFragmentManager)
        }
    }

    override fun onNotificationsClearedSuccessfully() {
        Toast.makeText(this, R.string.notificationsDeleteSuccessfully, Toast.LENGTH_SHORT).show()
        val user: User = UserRepository(this).getUser()!!
        presenter.loadNotifications(userId = user.id!!, limit = 10, skip = 0)
    }

    override fun onNotificationsClearFailed() {
        Toast.makeText(this, R.string.notificationsDeleteFailed, Toast.LENGTH_SHORT).show()
    }

    override fun onNotificationDeleteSuccessfully() {
        Toast.makeText(this, R.string.notificationDeleteSuccessfully, Toast.LENGTH_SHORT).show()
        val user: User = UserRepository(this).getUser()!!
        presenter.loadNotifications(userId = user.id!!, limit = 10, skip = 0)
    }

    override fun onNotificationDeleteFailed() {
        Toast.makeText(this, R.string.notificationDeleteFailed, Toast.LENGTH_SHORT).show()
    }
    /*Notifications Delete presenter ended*/

}