package com.almersal.android.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideRecyclerViewAdapter
import com.almersal.android.adapters.CategoryProfileRecyclerViewAdapter
import com.almersal.android.adapters.JobsSearchAdapter
import com.almersal.android.adapters.PostRecyclerViewAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerProfileComponent
import com.almersal.android.di.module.ProfileModule
import com.almersal.android.di.ui.ProfileContract
import com.almersal.android.di.ui.ProfilePresenter
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.dialogs.bottomSheetFragments.SubCategorySubscriptionBottomSheet
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.BindingUtils
import com.almersal.android.utilities.IntentHelper
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import kotlinx.android.synthetic.main.profile_layout.*
import java.util.HashMap
import javax.inject.Inject


class ProfileActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, ProfileContract.View,
    OnCategorySelectListener {

    @Inject
    lateinit var presenter: ProfilePresenter


    @BindView(R.id.myPostsStateLayout)
    lateinit var myPostsStateLayout: Stateslayoutview

    @BindView(R.id.myBusinessStateLayout)
    lateinit var myBusinessStateLayout: Stateslayoutview

    @BindView(R.id.myCategoriesStateLayout)
    lateinit var myCategoriesStateLayout: Stateslayoutview

    @BindView(R.id.profileImage)
    lateinit var mFab: ImageView

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.myCategories)
    lateinit var myCategories: RecyclerView

    @BindView(R.id.myPosts)
    lateinit var myPosts: RecyclerView

    @BindView(R.id.myBusiness)
    lateinit var myBusiness: RecyclerView

//    @BindView(R.id.genderTabLayout)
//    lateinit var genderTabLayout: TabLayout
//
//    @BindView(R.id.birthDate)
//    lateinit var birthDate: EditText


    private val percentageToShowImage = 20

    private var mMaxScrollSize: Int = 0
    private var mIsImageHidden: Boolean = false

    /*User information started*/

    @BindView(R.id.userName)
    lateinit var userName: TextView

    @BindView(R.id.adsCount)
    lateinit var adsCount: TextView

    @BindView(R.id.userEmail)
    lateinit var userEmail: TextView


    @BindView(R.id.emailText)
    lateinit var emailText: TextView


    /*User information ended*/


    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerViews() {
        myCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        myPosts.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        myBusiness.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun initTabLayout() {
//        val maleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
//                .setGender(R.string.male, R.mipmap.ic_male_24dp)).setText(R.string.male)
//        val femaleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
//                .setGender(R.string.female, R.mipmap.ic_female_24dp)).setText(R.string.female)
//
//        genderTabLayout.addTab(maleTab)
//        genderTabLayout.addTab(femaleTab)

        val onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.v("", "")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                    ?.setTextColor(ContextCompat.getColor(baseContext, R.color.grayLightTextColor))
                tab?.customView?.visibility = View.GONE
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                    ?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
                tab?.customView?.visibility = View.VISIBLE
            }
        }

//        genderTabLayout.addOnTabSelectedListener(onTabSelectedListener)
//
//        femaleTab.select()
//        maleTab.select()
//
//        genderTabLayout.removeOnTabSelectedListener(onTabSelectedListener)


    }

    private fun initDI() {
        val component = DaggerProfileComponent.builder()
            .profileModule(ProfileModule(this))
            .build()
        component.inject(this)
        presenter.attachView(this)
        presenter.subscribe()

        val criteria: MutableMap<String, Pair<String, String>> = HashMap()//[ownerId]
        val user = UserRepository(context = baseContext).getUser()!!
        criteria["where"] = Pair("ownerId", user.id!!)

        presenter.loadUserPosts(user.id!!)
        presenter.loadUserBusinesses(user.id!!)
        presenter.loadUserCategories(user)
        presenter.getUser(user.id!!)
        presenter.getJobsByOwner(user.id!!)


    }

    private fun bindInfo(user: User) {
        userName.text = user.username
        if (!user.CV?.primaryIdentifier.isNullOrBlank()) {
            emailText.text = getString(R.string.position_title)
            userEmail.text = user.CV?.primaryIdentifier
        } else {
            userEmail.text = user.email
        }

//        birthDate.setText((if (user.birthdate != null) user.birthdate!! else ""))
//        if ((user.gender != null) and user.gender!!.equals(UserGender.male.gender, false)) {
//            genderTabLayout.getTabAt(0)!!.select()
//        } else {
//            genderTabLayout.getTabAt(1)!!.select()
//        }
        BindingUtils.loadProfileImage(mFab, user.imageProfile)
        presenter.loadUserCategories(user)

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()
        initTabLayout()
        initDI()

        categoriesAddLink.setOnClickListener {
            addCategory()
        }
        appbar.addOnOffsetChangedListener(this)


        myCategoriesStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {

            }

            /*Instead of no subCategory Subscribed*/
            override fun onRequestPermission() {
                val subCategorySubscriptionBottomSheet: SubCategorySubscriptionBottomSheet =
                    SubCategorySubscriptionBottomSheet.getNewInstance(null)

                subCategorySubscriptionBottomSheet.show(
                    supportFragmentManager,
                    SubCategorySubscriptionBottomSheet.SubCategorySubscriptionBottomSheet_Tag
                )
            }
        })

        myPostsStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                val user = UserRepository(this@ProfileActivity).getUser()!!
                presenter.loadUserPosts(userId = user.id!!)
            }

            override fun onRequestPermission() {

            }
        })

        myBusinessStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                val user = UserRepository(this@ProfileActivity).getUser()!!
                presenter.loadUserBusinesses(userId = user.id!!)
            }

            override fun onRequestPermission() {

            }
        })

        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.SubCategorySubscriptionBottomSheet_Tag -> {

                    bindInfo(UserRepository(this@ProfileActivity).getUser()!!)

                }
            }
        }

    }


    fun addCategory() {

        val subCategorySubscriptionBottomSheet: SubCategorySubscriptionBottomSheet =
            SubCategorySubscriptionBottomSheet.getNewInstance(null)

        subCategorySubscriptionBottomSheet.show(
            supportFragmentManager,
            SubCategorySubscriptionBottomSheet.SubCategorySubscriptionBottomSheet_Tag
        )

    }

    override fun onResume() {
        super.onResume()
        val user = UserRepository(context = baseContext).getUser()!!
        bindInfo(user)
    }

    @OnClick(R.id.cvBtn)
    fun onCvBtnClicked() {
        val activityName = UserResumeActivity::class.java.canonicalName
        IntentHelper.startActivityByName(this, activityName)
    }

    @OnClick(R.id.profileImage)
    fun onProfileImageClick(view: View) {
        val user = UserRepository(context = baseContext).getUser()!!
        IntentHelper.startPictureActivity(this, user.imageProfile)
    }

    @OnClick(R.id.AdsAddLink)
    fun onAdsAddLinkClick(view: View) {
        IntentHelper.startPostAddActivity(context = baseContext)
        Log.v("Clicked", view.id.toString())
    }

    @OnClick(R.id.BusinessAddLink)
    fun onBusinessAddLink(view: View) {
        IntentHelper.startBusinessAddActivity(context = baseContext)
        Log.v("Clicked", view.id.toString())
    }

    @OnClick(R.id.settingBtn)
    fun onBusinessSettingsBtnClick(view: View) {
        IntentHelper.startSettingsActivity(context = baseContext)
        Log.v("Clicked", view.id.toString())
    }

    @OnClick(R.id.logoutBtn)
    fun onLogoutButtonClick(view: View) {
        UserRepository(baseContext).flushUser()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                val activityName = EditResumeActivity::class.java.canonicalName
                IntentHelper.startActivityByName(this, activityName)
            }
        }
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (appBarLayout == null)
            return

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.totalScrollRange

        val currentScrollPercentage = Math.abs(verticalOffset) * 100 / mMaxScrollSize

        if (currentScrollPercentage >= percentageToShowImage) {
            if (!mIsImageHidden) {
                mIsImageHidden = true

                ViewCompat.animate(mFab).scaleY(0f).scaleX(0f).start()
            }
        }

        if (currentScrollPercentage < percentageToShowImage) {
            if (mIsImageHidden) {
                mIsImageHidden = false
                ViewCompat.animate(mFab).scaleY(1f).scaleX(1f).start()
            }
        }
    }


    /*My post presenter started*/
    override fun showUserPostsProgress(show: Boolean) {
        if (show)
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        else
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun showUserPostsLoadErrorMessage(visible: Boolean) {
        if (visible)
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        else
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun showUserPostsEmptyView(visible: Boolean) {
        if (visible)
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        else
            myPostsStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
    }

    override fun onUserPostsListSuccessfully(postList: MutableList<Post>) {
        myPosts.adapter = PostRecyclerViewAdapter(baseContext, postList, true)
        adsCount.text = postList.size.toString()
        Log.v("", "")
    }
    /*My post presenter ended*/


    /*My Businesses guide presenter started*/
    override fun showUserBusinessesProgress(show: Boolean) {
        if (show) {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showUserBusinessesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showUserBusinessesEmptyView(visible: Boolean) {
        if (visible) {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            myBusinessStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onUserBusinessesListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {
        myBusiness.adapter =
            BusinessGuideRecyclerViewAdapter(baseContext, businessGuideList, null, true)
    }
    /*My Businesses guide presenter ended*/


    /*My Categories presenter started */
    override fun showUserCategoriesProgress(show: Boolean) {
        if (show) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showUserCategoriesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showUserCategoriesEmptyView(visible: Boolean) {
        if (visible) {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.NopermissionLayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onUserCategoriesListSuccessfully(categories: MutableList<Category>) {
        val adapter = CategoryProfileRecyclerViewAdapter(
            context = baseContext,
            categoriesList = categories, isClickable = true, onCategorySelectListener = this@ProfileActivity
        )
        myCategories.adapter = adapter
        adapter.clearAll()
    }
    /*My Categories presenter ended */


    /*Profile presenter started*/
    override fun showUpdateProfileProgress(show: Boolean) {
        if (show) {
            val progressDialog = ProgressDialog()
            progressDialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.closeDialog(supportFragmentManager)
        }
    }

    override fun showUpdateProfileLoadErrorMessage(visible: Boolean) {
        if (visible) {
            bindInfo(UserRepository(this).getUser()!!)
            Toast.makeText(this, resources.getString(R.string.checkInternetConnection), Toast.LENGTH_LONG).show()
        }
    }

    override fun onUpdateProfileSuccessfully(user: User) {
        val currentUser = UserRepository(this).getUser()!!
        user.token = currentUser.token
        UserRepository(this).addUser(user)
        bindInfo(user)
        Toast.makeText(this, resources.getString(R.string.SubscribedCategoriesUpdateSuccessfully), Toast.LENGTH_LONG)
            .show()
    }
    /*Profile presenter ended*/


    override fun onSelectCategory(category: Category) {

        val subCategorySubscriptionBottomSheet: SubCategorySubscriptionBottomSheet =
            SubCategorySubscriptionBottomSheet.getNewInstance(category)

        subCategorySubscriptionBottomSheet.show(
            supportFragmentManager,
            SubCategorySubscriptionBottomSheet.SubCategorySubscriptionBottomSheet_Tag
        )

//        val builder = AlertDialog.Builder(this)
//        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
//            when (which) {
//                DialogInterface.BUTTON_POSITIVE -> {
//                    val user = UserRepository(context = baseContext).getUser()!!
//                    if (user.postCategoriesIds == null)
//                        user.postCategoriesIds = mutableListOf()
//                    user.postCategoriesIds?.add(category.id)
//                    presenter.updateProfile(UserProfileMapper.userProfileTransform(user), user.token)
//                }
//                DialogInterface.BUTTON_NEGATIVE -> {
//                    (myCategories.adapter as CategoryProfileRecyclerViewAdapter).setCategoryItemStatus(category, false)
//                }
//            }
//        }
//        builder.setMessage(resources.getString(R.string.doYouWantFollowNotificationsUnderThisCategory))
//                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
//                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()
    }

    override fun onDeselectCategory(category: Category) {

        val subCategorySubscriptionBottomSheet: SubCategorySubscriptionBottomSheet =
            SubCategorySubscriptionBottomSheet.getNewInstance(category)

        subCategorySubscriptionBottomSheet.show(
            supportFragmentManager,
            SubCategorySubscriptionBottomSheet.SubCategorySubscriptionBottomSheet_Tag
        )


//        val builder = AlertDialog.Builder(this)
//        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
//            when (which) {
//                DialogInterface.BUTTON_POSITIVE -> {
//                    val user = UserRepository(context = baseContext).getUser()!!
//                    if (user.postCategoriesIds == null)
//                        user.postCategoriesIds = mutableListOf()
//                    user.postCategoriesIds?.remove(category.id)
//                    presenter.updateProfile(UserProfileMapper.userProfileTransform(user), user.token)
//                }
//                DialogInterface.BUTTON_NEGATIVE -> {
//                    (myCategories.adapter as CategoryProfileRecyclerViewAdapter).setCategoryItemStatus(category, true)
//                }
//            }
//        }
//        builder.setMessage(resources.getString(R.string.doYouWantUnFollowNotificationsUnderThisCategory))
//                .setPositiveButton(resources.getString(R.string.Yes), dialogClickListener)
//                .setNegativeButton(resources.getString(R.string.No), dialogClickListener).show()

    }


    override fun updateUserInfo(user: User?) {
        bindInfo(user ?: User())

    }
    override fun onJobsLoaded(jobs: MutableList<Job>) {
        jobsRecycler.layoutManager = LinearLayoutManager(this)
        jobsRecycler.adapter = JobsSearchAdapter(this, jobs)
        if (jobs.isNullOrEmpty()) {
            jobsPlaceHolder.visibility = View.VISIBLE
            jobsRecycler.visibility = View.GONE
        } else {
            jobsPlaceHolder.visibility = View.GONE
            jobsRecycler.visibility = View.VISIBLE
        }
    }


}