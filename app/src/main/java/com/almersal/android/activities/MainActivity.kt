package com.almersal.android.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.*
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.PostRecyclerViewAdapter
import com.almersal.android.adapters.PostSliderRecyclerViewAdapter
import com.almersal.android.adapters.TagsRecyclerViewAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.di.component.DaggerMainComponent
import com.almersal.android.di.module.NotificationModule
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.module.VolumesModule
import com.almersal.android.di.ui.*
import com.almersal.android.dialogs.bottomSheetFragments.SubCategorySubscriptionBottomSheet
import com.almersal.android.enums.FilterType
import com.almersal.android.enums.UserStatus
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnTagSelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.repositories.SettingRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.utilities.MyBounceInterpolator
import com.almersal.android.views.SelectedTagsView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.NotificationBadge
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.github.florent37.viewanimator.ViewAnimator
import com.skyfishjy.library.RippleBackground
import kotlinx.android.synthetic.main.main_layout.*
import javax.inject.Inject
import java.util.*


class MainActivity : BaseActivity(), VolumesContract.View,
    PostContract.View, NotificationContract.View, OnTagSelectListener {

    @Inject
    lateinit var presenter: VolumesPresenter

    @Inject
    lateinit var postPresenter: PostPresenter

    @Inject
    lateinit var notificationPresenter: NotificationPresenter

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

    @BindView(R.id.nextBtn)
    lateinit var nextBtn: View

    @BindView(R.id.previousBtn)
    lateinit var previousBtn: View

    @BindView(R.id.main_appbar)
    lateinit var main_appbar: AppBarLayout

    @BindView(R.id.addPostBtnContainer)
    lateinit var addPostBtnContainer: RippleBackground


    @BindView(R.id.newsPaperBtn)
    lateinit var newsPaperBtn: AppCompatButton

    @BindView(R.id.marketBtn)
    lateinit var marketBtn: AppCompatButton

    @BindView(R.id.loginBtn)
    lateinit var loginBtn: ImageView


    var featuredPosition: Int = 0
    var featuredDirectionInc = true
    val featuredPostsTimer: Timer = Timer()
    var marketSelected = false

    var limit = 100
    var pageId = 0

    private fun initToolBar() {
        toolBar.setTitle(R.string.app_name)
        setSupportActionBar(toolBar)
    }

    private fun initDI() {
        val component = DaggerMainComponent.builder()
            .notificationModule(NotificationModule(this))
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

        notificationPresenter.attachView(this)
        notificationPresenter.subscribe()
        val user: User? = UserRepository(baseContext).getUser()
        if (user != null) {
            notificationPresenter.loadUnSeenNotifications(user.id!!)
            loadProfileImage(user)
        }

    }

    override fun onResume() {
        super.onResume()
        val user: User? = UserRepository(baseContext).getUser()
        if (user != null) {
            notificationPresenter.loadUnSeenNotifications(user.id!!)
            loadProfileImage(user)
        }
    }

    private fun initRecyclerView() {
        val snapHelper = LinearSnapHelper()
        featuredPostsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(featuredPostsRecyclerView)

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummyDataRepositories.getTagsDefaultRepositories()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this

        volumesRecyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        volumesRecyclerView.adapter = PostRecyclerViewAdapter(MainActivity@ this, mutableListOf())
    }

    private fun loadProfileImage(user: User) {
        BindingUtils.loadProfileThumbnailImage(loginBtn, user.imageProfile)
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.main_layout)
        val extras = intent.extras?:Bundle()
        with(extras) {
            val volumeId = getString("volumeId","")
            val marketProductId = getString("marketProductId","")
            val businessId = getString("businessId","")
            val jobId = getString("jobId","")
            val adId = getString("adId","")

            when {
                volumeId.isNotEmpty() -> {
                    IntentHelper.startVolumesActivity(
                        this@MainActivity,
                        volumeId
                    )

                }
                marketProductId.isNotEmpty() -> IntentHelper.startProductDetailsActivity(
                    this@MainActivity,
                    marketProductId
                )
                businessId.isNotEmpty() -> IntentHelper.startBusinessGuideDetailsActivity(
                    this@MainActivity,
                    businessId
                )
                jobId.isNotEmpty() -> IntentHelper.startJobDetailsActivity(
                    this@MainActivity,
                    jobId
                )
                adId.isNotEmpty() -> IntentHelper.startPostDetailsActivity(
                    this@MainActivity,
                    adId
                )
            }
        }
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerView()
        initDI()

        badge.setNumber(0, true)

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


        /*First time home page*/
        if (SettingRepositories(this).getFirstSubscription() &&
            (UserRepository(this).getUser() != null)
        ) {
            val dialog = SubCategorySubscriptionBottomSheet.getNewInstance(null)
            dialog.show(
                supportFragmentManager,
                SubCategorySubscriptionBottomSheet.SubCategorySubscriptionBottomSheet_Tag
            )
        }


        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.Post_Filter_Activity_Tag -> {
                    setFilterEntity(it.message as FilterEntity)
                }
            }
        }


        Thread {
            var fin = false
            try {
                while (true) {
                    if (fin)
                        break

                    runOnUiThread {
                        fin = isFinishing
                        val btn: View = findViewById(R.id.addPostBtn)
                        ViewAnimator.animate(btn).rotation(180f, 0.0f/*Math.PI.toFloat()*/)
                            .scale(0.5f, 1.0f)
                            .duration(200)
                            .onStart {
                                addPostBtnContainer.startRippleAnimation()
                            }
                            .onStop {
                                addPostBtnContainer.stopRippleAnimation()
                            }
                            .start()
                        //                    .flash()
                        //                    .repeatCount(-1)
                        //                    .repeatMode(ValueAnimator.REVERSE)
                        //                    .startDelay(5000)
                        //                    .startDelay(5000)
                    }
                    Thread.sleep(5000)
                }
            } catch (ex: Exception) {
                Log.v("", "")
                Log.v("", "")
            }
        }.start()


    }

    private fun setFilterEntity(filterEntity: FilterEntity) {
        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(baseContext, filterEntity.getTags()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this
        if (marketSelected) {
            val categoryId = filterEntity.category?.id
            val subcategoryId = filterEntity.subCategory?.id
            val keyword = filterEntity.query
            pageId = 0
            (volumesRecyclerView.adapter as PostRecyclerViewAdapter).filterByCriteria(filterEntity,true)
            presenter.loadProducts(keyword,categoryId, subcategoryId, limit * pageId, limit)
        } else {

            if (volumesRecyclerView.adapter != null) {
                (volumesRecyclerView.adapter as PostRecyclerViewAdapter).filterByCriteria(filterEntity,false)
            }

        }
    }


    /*Main Activity Events started*/

    @OnClick(R.id.marketBtn)
    fun onMarketBtnClicked(view: View) {

        if (!marketSelected) {
            selectedTagsView.setAdapter(
                TagsRecyclerViewAdapter(
                    this,
                    DummyDataRepositories.getTagsDefaultRepositories(false)
                )
            )
            marketSelected = true
            browse_Container.visibility = View.GONE
            val m1 = ValueAnimator.ofFloat(1f, 2f)
            m1.duration = 1000
            m1.interpolator = AnticipateOvershootInterpolator()

            m1.addUpdateListener {
                val lp1 = marketBtn.layoutParams as LinearLayout.LayoutParams
                val lp2 = newsPaperBtn.layoutParams as LinearLayout.LayoutParams

                lp1.weight = it.animatedValue as Float
                lp2.weight = 3 - it.animatedValue as Float

                marketBtn.layoutParams = lp1
                newsPaperBtn.layoutParams = lp2

            }
            m1.start()



            ViewCompat.setBackgroundTintList(
                marketBtn,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
            )
            ViewCompat.setBackgroundTintList(
                newsPaperBtn,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grayLightTextColor))
            )
            pageId = 0
            presenter.loadProducts(limit = limit, skip = limit * pageId)

            volumesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {


                    val lm = volumesRecyclerView.layoutManager as StaggeredGridLayoutManager
                    val visibleItemCount = lm.childCount
                    val totalItemCount = lm.itemCount


                    val firstVisibleItems = lm.findFirstCompletelyVisibleItemPositions(null)
                    var pastVisibleItems = 0
                    if (firstVisibleItems != null && firstVisibleItems.isNotEmpty()) {
                        pastVisibleItems = firstVisibleItems[0]
                    }
                    if (stateLayout.currentState != LayoutStatesEnum.Waitinglayout) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            pageId++
                            presenter.loadProducts(
                                limit = limit,
                                skip = limit * pageId

                            )
                        }
                    }
                }
            })
        }


    }


    @OnClick(R.id.newsPaperBtn)
    fun onNewspaperBtnClicked(view: View) {

        if (marketSelected) {
            selectedTagsView.setAdapter(
                TagsRecyclerViewAdapter(
                    this,
                    DummyDataRepositories.getTagsDefaultRepositories()
                )
            )
            volumesRecyclerView.clearOnScrollListeners()
            browse_Container.visibility = View.VISIBLE
            val m1 = ValueAnimator.ofFloat(1f, 2f)
            m1.duration = 1000
            m1.interpolator = AnticipateOvershootInterpolator()
            m1.addUpdateListener {
                val lp1 = marketBtn.layoutParams as LinearLayout.LayoutParams
                val lp2 = newsPaperBtn.layoutParams as LinearLayout.LayoutParams

                lp2.weight = it.animatedValue as Float
                lp1.weight = 3 - it.animatedValue as Float

                marketBtn.layoutParams = lp1
                newsPaperBtn.layoutParams = lp2

            }
            m1.start()
            ViewCompat.setBackgroundTintList(
                newsPaperBtn,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue))
            )
            ViewCompat.setBackgroundTintList(
                marketBtn,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grayLightTextColor))
            )
            marketSelected = false;
            presenter.loadDefaultVolume()
        }
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


    @OnClick(R.id.jobsBtn)
    fun onJobsBtnClicked(view: View) {
        val activityName = JobsSearchActivity::class.java.canonicalName
        IntentHelper.startActivityByName(this, activityName)
//        IntentHelper.startDutyPharmacyActivity(this)
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

    @OnClick(R.id.addPostBtn)
    fun onAddPostClick(view: View) {
        val user = UserRepository(this).getUser()
        if (user != null)
            IntentHelper.startPostAddActivity(this)
        else
            IntentHelper.startSignInActivity(this)
    }

    @OnClick(R.id.notificationBtn)
    fun onNotificationBadgeClick(view: View) {
        val user = UserRepository(this).getUser()
        if (user != null)
            IntentHelper.startNotificationActivity(this)
        else
            IntentHelper.startSignInActivity(this)
    }
    /*Main Activity Events ended*/


    /*Filter actions started*/
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
        val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(
            MainActivity@ this,
            list,
            if (marketSelected) FilterType.ProductFilter else FilterType.PostFilter
        )
        Log.v("View Clicked", view.id.toString())
    }

    override fun onSelectTag(tagEntity: TagEntity) {
        if (volumesRecyclerView.adapter != null) {
            val adapter = volumesRecyclerView.adapter as PostRecyclerViewAdapter
            adapter.excludeFilter(tagEntity,marketSelected)
            selectedTagsView.setAdapter(
                (TagsRecyclerViewAdapter(
                    baseContext, adapter.filterEntity!!.getTags(),
                    onTagSelectListener = this
                ))
            )
        }
        Log.v("", "")
    }

    override fun onTagClick(tagEntity: TagEntity) {
        val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(
            MainActivity@ this,
            list,
            if (marketSelected) FilterType.ProductFilter else FilterType.PostFilter
        )
    }

    /*Filter actions ended*/


//    @Optional
//    @OnClick(R.id.searchContainer)
//    fun onSearchContainerClick(view: View) {
////        main_appbar.setExpanded(false)
//    }
//
//    @Optional
//    @OnTouch(R.id.searchContainer)
//    fun onSearchContainerTouch(view: View, moveEvent: MotionEvent): Boolean {
////        main_appbar.setExpanded(false)
//        return false
//    }


    /*Volume Presenter started*/
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
//            main_appbar.setExpanded(true)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun bindProducts(products: MutableList<Product>) {
        var adapter: PostRecyclerViewAdapter
        if (pageId == 0) {
            adapter = PostRecyclerViewAdapter(MainActivity@ this, products = products)
            volumesRecyclerView.adapter = adapter
        } else
            (volumesRecyclerView.adapter as PostRecyclerViewAdapter).addProducts(products)

    }

    override fun loadedData(volume: Volume) {
        volumeTitle.text = volume.getTitle()

        val filter = (volumesRecyclerView.adapter as PostRecyclerViewAdapter).filterEntity
        val adapter = PostRecyclerViewAdapter(
            MainActivity@ this,
            volume.posts.filter { it.status == UserStatus.active.status }.toMutableList()
        )
        volumesRecyclerView.adapter = adapter
        if (filter != null)
            adapter.filterByCriteria(filter,false)
    }

    override fun noMoreData() {
        Toast.makeText(this, R.string.noMoreVolumeFound, Toast.LENGTH_LONG).show()
    }

    override fun disableNext() {
        nextBtn.isEnabled = false
        nextBtn.isClickable = false
        nextBtn.alpha = 0.5f
    }

    override fun enableNext() {
        nextBtn.isEnabled = true
        nextBtn.isClickable = true
        nextBtn.alpha = 1.0f

    }

    override fun disablePrev() {
        previousBtn.isEnabled = false
        previousBtn.isClickable = false
        previousBtn.alpha = 0.5f
    }

    override fun enablePrev() {
        previousBtn.isEnabled = true
        previousBtn.isClickable = true
        previousBtn.alpha = 1.0f
    }

    /*Volume Presenter ended*/


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

        featuredPostsTimer.scheduleAtFixedRate(AutoScrollTask(), 2000, 5000)
    }
    /*Post Presenter ended*/


    /*Notification presenter started*/
    override fun showNotificationProgress(show: Boolean) {
        super.showNotificationProgress(show)
    }

    override fun showNotificationLoadErrorMessage(visible: Boolean) {
        super.showNotificationLoadErrorMessage(visible)
    }

    override fun showNotificationEmptyView(visible: Boolean) {
        if (visible) {
            badge.setNumber(0, true)
        }
    }

    override fun onSeenNotificationsLoaded(notificationList: MutableList<NotificationEntity>) {
        badge.setNumber(notificationList.size, true)
        Log.v("", "")
    }
    /*Notification presenter ended*/

    private inner class AutoScrollTask : TimerTask() {

        override fun run() {
            var end = false
            var itemsCount = featuredPostsRecyclerView.getAdapter()?.getItemCount()
            if (featuredPostsRecyclerView.getAdapter() != null && itemsCount != null) {
                if (featuredDirectionInc) {
                    if (featuredPosition >= itemsCount - 1) {
                        featuredPosition--
                        featuredDirectionInc = false
                    } else {
                        featuredPosition++
                    }
                } else {
                    if (featuredPosition <= 0) {
                        featuredPosition++
                        featuredDirectionInc = true
                    } else {
                        featuredPosition--
                    }
                }
            }

            print("featuredPosition is" + featuredPosition)
            featuredPostsRecyclerView.smoothScrollToPosition(featuredPosition)
        }
    }

}