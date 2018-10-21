package com.brainsocket.globalpages.activities

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
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.MediaViewPagerAdapter
import com.brainsocket.globalpages.data.entities.Post
import com.brainsocket.globalpages.dialogs.ContactPostDialog
import com.brainsocket.globalpages.normalization.DateNormalizer
import com.brainsocket.globalpages.repositories.UserRepository
import com.brainsocket.globalpages.utilities.IntentHelper
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator
import com.google.gson.Gson


class PostDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val Post_Tag = "Post_Tag"
    }

    lateinit var post: Post

    @BindView(R.id.mediaViewPager)
    lateinit var mediaViewPager: ViewPager

    @BindView(R.id.postCategory)
    lateinit var postCategory: TextView

    @BindView(R.id.postSubCategory)
    lateinit var postSubCategory: TextView

    @BindView(R.id.postCreatedDate)
    lateinit var postCreatedDate: TextView

    @BindView(R.id.postTitle)
    lateinit var postTitle: TextView

    @BindView(R.id.postCity)
    lateinit var postCity: TextView

    @BindView(R.id.postLocation)
    lateinit var postLocation: TextView

    @BindView(R.id.postInLocation)
    lateinit var postInLocation: TextView

    @BindView(R.id.postDescription)
    lateinit var postDescription: TextView

    @BindView(R.id.viewPagerIndicator)
    lateinit var viewPagerIndicator: CircleIndicator

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

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
        val json = intent.getStringExtra(Post_Tag)
        post = Gson().fromJson(json, Post::class.java)

        appbar.addOnOffsetChangedListener(this)
        initToolBar()

        bindInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val user = UserRepository(this).getUser()
        if (user != null) {
            val id = user.id
            if (post.ownerId == id)
                menuInflater.inflate(R.menu.post_menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
                IntentHelper.startPostEditActivity(this@PostDetailsActivity, post)
            }
        }
        return false
    }

    @Optional
    @OnClick(R.id.contactBtn, R.id.contactTextBtn)
    fun onContactButtonClick(view: View) {
        val contactDialog = ContactPostDialog.newInstance(post.owner.phoneNumber)
        contactDialog.show(supportFragmentManager, ContactPostDialog.ContactPostDialog_Tag)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    private fun bindInfo() {
        try {
            mediaViewPager.adapter = MediaViewPagerAdapter(this, post.media)
            viewPagerIndicator.setViewPager(mediaViewPager)

            postCategory.text = post.category.getTitle()
            postSubCategory.text = post.subCategory.getTitle()

            postTitle.text = post.title
            postCity.text = post.city.getTitle()
            postLocation.text = post.location.getTitle()

            val inLocation = resources.getString(R.string.In) + " " + post.location.getTitle()
            postInLocation.text = inLocation

            postDescription.text = post.description
            postCreatedDate.text = DateNormalizer.getCanonicalDateFormat(post.creationDate)
        } catch (ex: Exception) {
        }
    }


}