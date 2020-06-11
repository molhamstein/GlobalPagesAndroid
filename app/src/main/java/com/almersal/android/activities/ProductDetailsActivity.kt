package com.almersal.android.activities

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.data.entities.Product
import com.almersal.android.data.entities.ProductThumb
import com.almersal.android.data.entities.Status
import com.almersal.android.di.component.DaggerProductDetailsComponent
import com.almersal.android.di.module.ProductModule
import com.almersal.android.di.ui.*
import com.almersal.android.dialogs.ContactPostDialog
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.AnalyticsEvents
import com.almersal.android.viewModel.ProductViewHolder
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.google.gson.Gson
import kotlinx.android.synthetic.main.post_details_layout.*

import javax.inject.Inject


class ProductDetailsActivity : BaseActivity(), ProductContract.View,
    AppBarLayout.OnOffsetChangedListener {


    companion object {
        const val ProductManageActivity_Tag = "ProductManageActivity_Tag"
        const val PostDetailsActivity_Tag = "PostDetailsActivity_Tag"
        const val PostDetailsActivity_Id_Tag = "PostDetailsActivity_Id_Tag"
        const val PostDetailsActivity_Id_Type = "PostDetailsActivity_Id_Type"
    }

    var product: Product? = null
    var menuItem: MenuItem? = null
    lateinit var productViewHolder: ProductViewHolder

    lateinit var productThumb: ProductThumb


    @BindView(R.id.flexible_example_appbar)
    lateinit var appbar: AppBarLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview


    @BindView(R.id.price)
    lateinit var price: TextView

    @Inject
    lateinit var presenter: ProductPresenter

    private fun initToolBar() {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initDI() {
        val component = DaggerProductDetailsComponent.builder()
            .productModule(ProductModule(this))
            .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.post_menu, menu)
        menuItem = menu?.findItem(R.id.menu_edit)
        toggleEditMode()
        return true
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.post_details_layout)
        ButterKnife.bind(this)
        price.visibility = View.VISIBLE
        initToolBar()
        appbar.addOnOffsetChangedListener(this)
        initDI()

        deactivate.setOnClickListener {
            presenter.deactivateProduct(product?.id ?: "")
        }

        productViewHolder = ProductViewHolder(findViewById(android.R.id.content))

        val json = intent.getStringExtra(PostDetailsActivity_Tag)
        if (!json.isNullOrBlank()) {
            product = Gson().fromJson(json, Product::class.java)
            val bundle = Bundle() ;
            bundle.putString("product_id",product?.id)
            mFirebaseAnalytics.logEvent(AnalyticsEvents.PRODUCT_DETAILS_OPENED,bundle)
            productViewHolder.bind(product!!)
            val user = UserRepository(this).getUser()
            deactivateBtn.visibility =
                if (product!!.ownerId == user?.id && product!!.status == "activated") View.VISIBLE else View.INVISIBLE

        } else {

            val id = intent.getStringExtra(PostDetailsActivity_Id_Tag)

            presenter.loadProduct(id)


            stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
                override fun onRefresh() {
                    val id = intent.getStringExtra(PostDetailsActivity_Id_Tag)
                    presenter.loadProduct(id)
                }

                override fun onRequestPermission() {

                }
            })

            fowardBtn.setOnClickListener {
                val currnetItem = mediaViewPager.currentItem
                mediaViewPager.currentItem =
                    if (currnetItem < mediaViewPager.childCount - 1) currnetItem + 1 else 0

            }
            backBtn.setOnClickListener {
                val currnetItem = mediaViewPager.currentItem
                mediaViewPager.currentItem =
                    if (currnetItem > 0) currnetItem - 1 else mediaViewPager.childCount
            }

        }

    }

    private fun toggleEditMode() {
        val user = UserRepository(this).getUser()
        if ((user != null) && (product != null) && (user.id != null)) {
            menuItem?.isVisible = (product!!.ownerId == user.id)
        } else
            menuItem?.isVisible = false


    }


    @Optional
    @OnClick(R.id.contactBtn, R.id.contactTextBtn)
    fun onContactButtonClick(view: View) {
        if (product != null) {
            val bundle = Bundle() ;
            bundle.putString("product_id",product?.id)
            mFirebaseAnalytics.logEvent(AnalyticsEvents.SHOW_PRODUCT_CONTACT,bundle)
            val contactDialog =
                ContactPostDialog.newInstance(
                    product!!.business?.phone1 ?: product!!.business?.phone1
                    ?: product!!.owner.phoneNumber
                )
            contactDialog.show(supportFragmentManager, ContactPostDialog.ContactPostDialog_Tag)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_edit -> {
//                IntentHelper.startProductAddActivity(this, product!!)
            }
        }
        return false
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }


    override fun onProductLoadedSuccessfully(product: Product) {
        this.product = product
        productViewHolder.bind(product)
        val user = UserRepository(this).getUser()
        deactivateBtn.visibility =
            if (product!!.ownerId == user?.id &&  product!!.status == "activated") View.VISIBLE else View.INVISIBLE
    }

    override fun onProductDeactivatedSuccessfully() {
        Toast.makeText(this, getString(R.string.product_deactivated), Toast.LENGTH_SHORT).show()
        onBackPressed()
    }
}
