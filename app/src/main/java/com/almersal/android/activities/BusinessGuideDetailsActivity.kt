package com.almersal.android.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideProductRecyclerViewAdapter
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entitiesModel.ProductThumbEditModel
import com.almersal.android.data.mapping.ProductProductModelTransformation
import com.almersal.android.di.component.DaggerBusinessGuideDetailsComponent
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.ui.BusinessGuidesContract
import com.almersal.android.di.ui.BusinessGuidesPresenter
import com.almersal.android.dialogs.ContactDialog
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewModel.BusinessGuideViewHolder
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.google.gson.Gson
import javax.inject.Inject

class BusinessGuideDetailsActivity : BaseActivity(), AppBarLayout.OnOffsetChangedListener,
        BusinessGuidesContract.View {

    companion object {
        const val BusinessGuideDetailsActivity_Tag = "BusinessGuideDetailsActivity_Tag"
        const val BusinessGuideDetailsActivity_Id_Tag = "BusinessGuideDetailsActivity_Id_Tag"
    }

    var businessGuide: BusinessGuide? = null

    lateinit var businessGuideViewHolder: BusinessGuideViewHolder

    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview



    var menuItem: MenuItem? = null

    @Inject
    lateinit var presenter: BusinessGuidesPresenter

    private fun initDI() {
        val component = DaggerBusinessGuideDetailsComponent.builder()
                .businessGuidesModule(BusinessGuidesModule(this))
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
        setContentView(R.layout.business_guide_details_layout)
        ButterKnife.bind(this)
        initToolBar()
        appbar.addOnOffsetChangedListener(this)
        initDI()

        businessGuideViewHolder = BusinessGuideViewHolder(findViewById(android.R.id.content))
        val jSon = intent.getStringExtra(BusinessGuideDetailsActivity_Tag)
        if (!jSon.isNullOrBlank()) {
            businessGuide = Gson().fromJson(jSon, BusinessGuide::class.java)
            businessGuideViewHolder.bind(businessGuide!!)
        } else {
            val id = intent.getStringExtra(BusinessGuideDetailsActivity_Id_Tag)
            presenter.loadBusinessGuideById(id)
        }

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

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                val id = intent.getStringExtra(BusinessGuideDetailsActivity_Id_Tag)
                presenter.loadBusinessGuideById(id)
            }

            override fun onRequestPermission() {

            }
        })

    }


   private fun toggleEditMode() {
        val user = UserRepository(this).getUser()
        if ((user != null) && (businessGuide != null))
            menuItem?.isVisible = (businessGuide!!.ownerId == user.id)

    }

    @OnClick(R.id.locationBtn)
    fun onLocationButtonClick(view: View) {
        IntentHelper.startLocationViewerActivity(this@BusinessGuideDetailsActivity, businessGuide!!.locationPoint)
    }

    @OnClick(R.id.ProductAddLink)
    fun onProductAddLinkClick(view: View) {
        IntentHelper.startProductAddActivity(baseContext, businessGuide!!)
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_business_details, menu)
        menuItem = menu?.findItem(R.id.business_edit)

        val user = UserRepository(this).getUser()
        if ((user != null) && (businessGuide != null))
            menuItem?.isVisible = (businessGuide!!.ownerId == user.id)

        toggleEditMode()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.business_edit -> {
                IntentHelper.startBusinessGuideEditActivity(baseContext, businessGuide!!)
            }
        }
        return true
    }

    @Optional
    @OnClick(R.id.contactContainer, R.id.contactBtn)
    fun onContactContainerClick(view: View) {
        val contactDialog = ContactDialog.newInstance(businessGuide!!.phone1,
                businessGuide!!.phone2, businessGuide!!.fax)
        contactDialog.show(supportFragmentManager, ContactDialog.ContactDialog_Tag)
        Log.v("View Clicked", view.id.toString())
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

    override fun onLoadBusinessGuide(businessGuide: BusinessGuide) {
        this.businessGuide = businessGuide
        businessGuideViewHolder.bind(businessGuide)
        toggleEditMode()

    }
    /*Presenter ended*/


}