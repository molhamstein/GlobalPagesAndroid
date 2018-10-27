package com.brainsocket.globalpages.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.PostRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.data.entities.User
import com.brainsocket.globalpages.di.component.DaggerProfileComponent
import com.brainsocket.globalpages.di.module.ProfileModule
import com.brainsocket.globalpages.di.ui.ProfileContract
import com.brainsocket.globalpages.di.ui.ProfilePresenter
import com.brainsocket.globalpages.enums.UserGender
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.BindingUtils
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.globalpages.views.CustomTabView
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import java.util.HashMap
import javax.inject.Inject


class ProfileActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, ProfileContract.View {

    @Inject
    lateinit var presenter: ProfilePresenter

    @BindView(R.id.myPostsStateLayout)
    lateinit var myPostsStateLayout: Stateslayoutview

    @BindView(R.id.myBusinessStateLayout)
    lateinit var myBusinessStateLayout: Stateslayoutview

    @BindView(R.id.myCategoriesStateLayout)
    lateinit var myCategoriesStateLayout: Stateslayoutview

    @BindView(R.id.flexible_example_fab)
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

    @BindView(R.id.genderTabLayout)
    lateinit var genderTabLayout: TabLayout

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

    @BindView(R.id.birthDate)
    lateinit var birthDate: EditText


    @BindView(R.id.AdsAddLink)
    lateinit var AdsAddLink: TextView


    /*User information ended*/

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerViews() {
        myCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        myCategories.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList())

        myPosts.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        myPosts.adapter = PostRecyclerViewAdapter(this, DummyDataRepositories.getPostList(), true)

        myBusiness.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        myBusiness.adapter = BusinessGuideRecyclerViewAdapter(this, DummyDataRepositories.getBusinessGuideList())
    }

    private fun initTabLayout() {
        val maleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.male, R.mipmap.ic_male_24dp)).setText(R.string.male)
        val femaleTab = genderTabLayout.newTab().setCustomView(CustomTabView(context = baseContext)
                .setGender(R.string.female, R.mipmap.ic_female_24dp)).setText(R.string.female)

        genderTabLayout.addTab(maleTab)
        genderTabLayout.addTab(femaleTab)

        val onTabSelectedListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.v("", "")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.grayLightTextColor))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(android.R.id.text1)
                        ?.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
            }
        }

        genderTabLayout.addOnTabSelectedListener(onTabSelectedListener)

        femaleTab.select()
        maleTab.select()

        genderTabLayout.removeOnTabSelectedListener(onTabSelectedListener)

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

        bindInfo(user)
    }

    private fun bindInfo(user: User) {
        userName.text = user.username
        userEmail.text = user.email
        birthDate.setText((if (user.birthdate != null) user.birthdate!! else ""))
        if ((user.gender != null) and user.gender!!.equals(UserGender.male.gender, false)) {
            genderTabLayout.getTabAt(0)!!.select()
        } else {
            genderTabLayout.getTabAt(1)!!.select()
        }
        BindingUtils.loadProfileImage(mFab, user.imageProfile)

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.profile_layout)
        ButterKnife.bind(this)
        initToolBar()
        initRecyclerViews()
        initTabLayout()
        initDI()
        appbar.addOnOffsetChangedListener(this)

        AdsAddLink.movementMethod = LinkMovementMethod.getInstance()

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                IntentHelper.startProfileEditActivity(this)
            }
        }
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
//        val i = verticalOffset
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
        myBusiness.adapter = BusinessGuideRecyclerViewAdapter(baseContext, businessGuideList)
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
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            myCategoriesStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onUserCategoriesListSuccessfully(categories: MutableList<Category>) {
        myCategories.adapter = CategoryRecyclerViewAdapter(baseContext, categories)
    }
    /*My Categories presenter ended */


}