package com.almersal.android.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.adapters.MediaViewPagerAdapter
import com.almersal.android.data.entities.Post
import com.almersal.android.di.component.DaggerPostDetailsComponent
import com.almersal.android.di.module.PostModule
import com.almersal.android.di.ui.PostContract
import com.almersal.android.di.ui.PostPresenter
import com.almersal.android.dialogs.ContactPostDialog
import com.almersal.android.normalization.DateNormalizer
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewModel.PostViewHolder
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.google.gson.Gson
import kotlinx.android.synthetic.main.post_details_layout.*
import javax.inject.Inject


class PostDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener, PostContract.View {

    companion object {
        const val PostDetailsActivity_Tag = "PostDetailsActivity_Tag"
        const val PostDetailsActivity_Id_Tag = "PostDetailsActivity_Id_Tag"
    }

    var post: Post? = null

    var menuItem: MenuItem? = null

    lateinit var postViewHolder: PostViewHolder

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @Inject
    lateinit var presenter: PostPresenter

    private fun initDI() {
        val component = DaggerPostDetailsComponent.builder()
            .postModule(PostModule(this))
            .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
    }


    private fun initToolBar() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_details_layout)
        ButterKnife.bind(this)
        initToolBar()
        appbar.addOnOffsetChangedListener(this)
        initDI()

        postViewHolder = PostViewHolder(findViewById(android.R.id.content))

        val json = intent.getStringExtra(PostDetailsActivity_Tag)
        if (!json.isNullOrBlank()) {
            post = Gson().fromJson(json, Post::class.java)
            postViewHolder.bind(post!!)
        } else {
            val id = intent.getStringExtra(PostDetailsActivity_Id_Tag)
            presenter.loadPost(id)
        }

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                val id = intent.getStringExtra(PostDetailsActivity_Id_Tag)
                presenter.loadPost(id)
            }

            override fun onRequestPermission() {

            }
        })

        fowardBtn.setOnClickListener {
            val currnetItem = mediaViewPager.currentItem
            mediaViewPager.currentItem = if (currnetItem < mediaViewPager.childCount - 1) currnetItem + 1 else 0

        }
        backBtn.setOnClickListener {
            val currnetItem = mediaViewPager.currentItem
            mediaViewPager.currentItem = if (currnetItem > 0) currnetItem - 1 else mediaViewPager.childCount
        }

    }

    private fun toggleEditMode() {
        val user = UserRepository(this).getUser()
        if ((user != null) && (post != null))
            menuItem?.isVisible = (post!!.ownerId == user.id)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        menuItem = menu?.findItem(R.id.menu_edit)
        toggleEditMode()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                IntentHelper.startPostEditActivity(this@PostDetailsActivity, post!!)
            }
        }
        return false
    }

    @Optional
    @OnClick(R.id.contactBtn, R.id.contactTextBtn)
    fun onContactButtonClick(view: View) {
        val contactDialog = ContactPostDialog.newInstance(post!!.owner.phoneNumber)
        contactDialog.show(supportFragmentManager, ContactPostDialog.ContactPostDialog_Tag)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
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

    override fun onPostLoadedSuccessfully(post: Post) {
        this.post = post
        postViewHolder.bind(post)
        toggleEditMode()
    }
    /*Presenter ended*/

}