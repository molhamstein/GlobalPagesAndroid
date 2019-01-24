package com.almersal.android.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideProductRecyclerViewAdapter
import com.almersal.android.adapters.MediaViewPagerAdapter
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.data.entitiesModel.ProductThumbEditModel
import com.almersal.android.data.entitiesModel.ProductThumbModel
import com.almersal.android.data.mapping.ProductProductModelTransformation
import com.almersal.android.dialogs.ContactDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewModel.BusinessGuideViewHolder
import com.brainsocket.mainlibrary.ViewPagerIndicator.CircleIndicator.CircleIndicator
import com.google.gson.Gson

class BusinessGuideDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val BusinessGuideDetailsActivity_Tag = "BusinessGuideDetailsActivity_Tag"
    }

    lateinit var businessGuide: BusinessGuide

    lateinit var businessGuideViewHolder: BusinessGuideViewHolder

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
        setContentView(R.layout.business_guide_details_layout)
        ButterKnife.bind(this)
        val jSon = intent.getStringExtra(BusinessGuideDetailsActivity_Tag)
        businessGuide = Gson().fromJson(jSon, BusinessGuide::class.java)
        businessGuideViewHolder = BusinessGuideViewHolder(findViewById(android.R.id.content))


        appbar.addOnOffsetChangedListener(this)

        initToolBar()
        businessGuideViewHolder.bind(businessGuide)

        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.ProductAddActivity_Tag -> {
                    try {
                        val productThumbModel: ProductThumbEditModel = it.message as ProductThumbEditModel
                        (businessGuideViewHolder.businessGuideProductRecyclerView.adapter
                                as BusinessGuideProductRecyclerViewAdapter)
                                .addOrUpdateItem(ProductProductModelTransformation.productTransform(productThumbModel))
                    } catch (ex: Exception) {
                        Log.v("", "")
                    }
                }
            }
        }


    }

    @OnClick(R.id.locationBtn)
    fun onLocationButtonClick(view: View) {
        IntentHelper.startLocationViewerActivity(this@BusinessGuideDetailsActivity, businessGuide.locationPoint)
    }

    @OnClick(R.id.ProductAddLink)
    fun onProductAddLinkClick(view: View) {
        IntentHelper.startProductAddActivity(baseContext, businessGuide)
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val user = UserRepository(this).getUser()
        if (user != null) {
            val id = user.id
            if (businessGuide.ownerId == id)
                menuInflater.inflate(R.menu.menu_business_details, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.business_edit -> {
                IntentHelper.startBusinessGuideEditActivity(baseContext, businessGuide)
            }
        }
        return true
    }

    @Optional
    @OnClick(R.id.contactContainer, R.id.contactBtn)
    fun onContactContainerClick(view: View) {
        val contactDialog = ContactDialog.newInstance(businessGuide.phone1,
                businessGuide.phone2, businessGuide.fax)
        contactDialog.show(supportFragmentManager, ContactDialog.ContactDialog_Tag)
        Log.v("View Clicked", view.id.toString())
    }

}