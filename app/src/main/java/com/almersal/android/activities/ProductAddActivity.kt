package com.almersal.android.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.di.component.DaggerProductAddCommponent
import com.almersal.android.di.module.*
import com.almersal.android.di.ui.*
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.enums.MediaTypeEnum
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewModel.ProductAddViewHolder

import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.github.florent37.viewanimator.ViewAnimator
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_add_new_job.*

import kotlinx.android.synthetic.main.product_add_layout.*
import kotlinx.android.synthetic.main.product_add_layout.baseContainer
import kotlinx.android.synthetic.main.product_add_layout.businesses
import kotlinx.android.synthetic.main.product_add_layout.categoryStateLayout
import kotlinx.android.synthetic.main.product_add_layout.cityStateLayout
import kotlinx.android.synthetic.main.product_add_layout.stateLayout
import kotlinx.android.synthetic.main.product_add_layout.toolbar
import net.alhazmy13.mediapicker.Video.VideoPicker

class ProductAddActivity : BaseActivity(), ProductContract.View, TagsCollectionContact.View, AttachmentContract.View
    , OnCategorySelectListener, OnCitySelectListener {


    companion object {
        const val PLACE_PICKER_REQUEST = 1

        const val PICTURE_REQUEST = 100

        const val MAP_BUTTON_REQUEST_CODE = 101

        const val USER_ID_TAG: String = "USER_ID"

        const val ProductAddActivity_Tag = "ProductAddActivity_Tag"
        const val businessId_key = "businessId"
    }

    lateinit var productAddViewHolder: ProductAddViewHolder

    @Inject
    lateinit var tagsCollectionPresenter: TagsCollectionPresenter

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    @Inject
    lateinit var productPresenter: ProductPresenter
    var businessId: String? = null
    var product: Product? = null

    private fun initToolBar() {
        setSupportActionBar(toolbar as Toolbar)
        (toolbar as Toolbar).setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerViews() {
        productImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        productImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())



        productCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        productSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        productCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        productCities.adapter = CityRecyclerViewAdapter(this, DummyDataRepositories.getCityList())

        productLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        productLocations.adapter = LocationEntityRecyclerViewAdapter(this, DummyDataRepositories.getLocationList())

    }

    private fun initDI() {
        val component = DaggerProductAddCommponent.builder()
            .tagsCollectionModule(TagsCollectionModule(this))
            .productAddModule(ProductAddModule(this))
            .attachmentModule(AttachmentModule(this))
            .build()
        component.inject(this)

        tagsCollectionPresenter.attachView(this)
        tagsCollectionPresenter.subscribe()
        tagsCollectionPresenter.loadProductCategories()
        tagsCollectionPresenter.loadCities(withCache = true)

        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()

        productPresenter.attachView(this)
        productPresenter.subscribe()


        addAttachmentBtn.setOnClickListener { onAddAttachmentClick() }
        productAddBtn.setOnClickListener { onProductAddBtn() }
        adBackHomeBtn.setOnClickListener { onAddBackHomeClick() }

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.product_add_layout)

        initToolBar()
        initRecyclerViews()
        initDI()

        businessId = intent.getStringExtra(AddNewJobActivity.businessId_key)
        if (businessId != null) {
            businesses.visibility = View.GONE
        } else {
            businesses.visibility = View.VISIBLE
        }

        productAddViewHolder = ProductAddViewHolder(findViewById(android.R.id.content))

        val productString: String? = intent.extras?.getString(ProductAddActivity_Tag, null)
        if (!productString.isNullOrEmpty()) {
            product = Gson().fromJson(productString, Product::class.java)
            productAddViewHolder.bindProduct(product!!)
            productAddBtn.setText(R.string.Update)
        }

        categoryStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                tagsCollectionPresenter.loadProductCategories()
            }

            override fun onRequestPermission() {

            }
        })

        cityStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                tagsCollectionPresenter.loadCities(false)
            }

            override fun onRequestPermission() {

            }
        })

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                requestAction()
            }

            override fun onRequestPermission() {

            }
        })

    }

    fun requestAction() {
        if (productAddViewHolder.isValid()) {

            val token = UserRepository(baseContext).getUser()!!.token
            val userId = UserRepository(baseContext).getUser()!!.id
            if (productAddViewHolder.isAdd())
                productPresenter.addProduct(productAddViewHolder.getProductModel(userId ?: ""), token)
            else
                productPresenter.updateProduct(
                    productAddViewHolder.getProductModel(userId ?: "")
                    , token
                )
        }
    }


    fun onAddAttachmentClick() {
        Pix.start(this@ProductAddActivity, BusinessGuideAddActivity.PICTURE_REQUEST, 1)


    }


    fun onProductAddBtn() {
        requestAction()

    }


    fun onAddBackHomeClick() {
        finishAffinity()
        IntentHelper.startMainActivity(this)
    }

    private fun animate() {
//        resultContainer.visibility = View.VISIBLE
        ViewAnimator.animate(baseContainer).translationY(2000f)
            ./*alpha(1f, 0f).andAnimate(resultContainer).translationY(1000f, 0f)
        .alpha(0f, 1f).*/start()

//        resultContainer.visibility = View.VISIBLE
//        val baseAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "alpha", 1.0f, 0.0f)
//        val baseTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationY", 0.0f, -baseContainer.height.toFloat())
//        val baseTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(baseContainer, "translationZ", 0.0f, 10.0f)
//
//        //Hard code toolbar margin top
//        val resultAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "alpha", 0.0f, 1.0f)
//        val resultTranslationYAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationY", baseContainer.height.toFloat() - 56, .0f)
//        val resultTranslationZAnimator: ObjectAnimator = ObjectAnimator.ofFloat(resultContainer, "translationZ", 10.0f, 0.0f)
//
//        val animatorSet = AnimatorSet()
//        animatorSet.interpolator = LinearInterpolator()
//        animatorSet.duration = 2000
//        animatorSet.playTogether(baseAnimator, resultAnimator, baseTranslationYAnimator, baseTranslationZAnimator,
//                resultTranslationYAnimator, resultTranslationZAnimator)
//        animatorSet.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PostAddActivity.PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                }
                PostAddActivity.PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
                    Log.v("", "")
                }
                PostAddActivity.MAP_BUTTON_REQUEST_CODE -> {
                    Log.v("", "")
                }
                VideoPicker.VIDEO_PICKER_REQUEST_CODE -> {
                    val returnValue = data!!.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH)
                    attachmentPresenter.loadVideoAttachmentFile(File(returnValue[0]))
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this@ProductAddActivity, PostAddActivity.PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(
                        this@ProductAddActivity,
                        resources.getString(R.string.Approve_Permissions_To_Pick_Images),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }


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

    override fun onAddProductSuccessfully(product: Product) {
        productAddViewHolder.bindProduct(product)
        RxBus.publish(MessageEvent(EventActions.ProductAddActivity_Tag, product))
        animate()
        Log.v("", "")
    }


    override fun onAddProductFail() {
        Toast.makeText(this@ProductAddActivity, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }

    override fun onUpdateProductSuccessfully(product: Product) {
        productAddViewHolder.bindProduct(product)
        RxBus.publish(MessageEvent(EventActions.ProductAddActivity_Tag, product))
        animate()
        Log.v("", "")
    }

    override fun onUpdateProductFail() {
        Toast.makeText(this@ProductAddActivity, R.string.unexpectedErrorHappend, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }


    /*Post Presenter ended*/


    /*Tags Presenter started*/

    override fun showCitiesProgress(show: Boolean) {
        if (show) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showCitiesEmptyView(visible: Boolean) {
        if (visible) {
            cityStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            cityStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onCitiesLoaded(citiesList: MutableList<City>) {
        productCities.adapter =
            CityRecyclerViewAdapter(context = baseContext, citiesListList = citiesList, onCitySelectListener = this)
    }


    override fun showPostCategoriesProgress(show: Boolean) {
        if (show) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showPostCategoriesEmptyView(visible: Boolean) {
        if (visible) {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            categoryStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun onPostCategoriesLoaded(categoriesList: MutableList<PostCategory>) {
        productCategories.adapter = CategoryRecyclerViewAdapter(
            context = baseContext,
            categoriesList = categoriesList.toMutableList(), onCategorySelectListener = this
        )
        if (product != null)
            productAddViewHolder.bindProduct(product!!)
    }
    /*Tags presenter ended*/


    /*Attachment presenter started */
    override fun showAttachmentProgress(show: Boolean) {
        if (show) {
            val progressDialog: ProgressDialog? =
                supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
            progressDialog?.dialog?.dismiss()
            val dialog = ProgressDialog.newInstance()
            dialog.showDialog(supportFragmentManager)
        } else {
            val progressDialog: ProgressDialog? =
                supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
            progressDialog?.dialog?.dismiss()
        }
        Log.v("", "")
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {
        Log.v("", "")
    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {
        Log.v("", "")
    }

    override fun onLoadAttachmentListSuccessfully(filePath: String, thumbnail: String) {
        (productImages.adapter as AttachmentRecyclerViewAdapter)
            .addItem(Attachment(filePath, MediaTypeEnum.IMAGES.type, thumbnail))
        Toast.makeText(baseContext, R.string.uploadFileSuccessfully, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }


    /*Attachment presenter ended*/


    override fun onSelectCategory(category: Category) {
        productSubCategories.adapter = SubCategoryRecyclerViewAdapter(
            context = baseContext,
            subCategoriesList = category.subCategoriesList
        )
    }

    override fun onSelectCity(city: City) {
        productLocations.adapter = LocationEntityRecyclerViewAdapter(baseContext, city.locations)
    }


}