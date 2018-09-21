package com.brainsocket.globalpages.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.CategoryRecyclerViewAdapter
import com.brainsocket.globalpages.adapters.SubCategoryRecyclerViewAdapter
import com.google.android.gms.location.places.ui.PlacePicker
import android.widget.Toast
import com.brainsocket.globalpages.adapters.AttachmentRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.Attachment
import com.brainsocket.globalpages.data.entities.BusinessGuide
import com.brainsocket.globalpages.data.entities.BusinessGuideCategory
import com.brainsocket.globalpages.data.entities.Category
import com.brainsocket.globalpages.di.component.DaggerBusinessGuideAddComponent
import com.brainsocket.globalpages.di.module.AttachmentModule
import com.brainsocket.globalpages.di.module.BusinessGuidesModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.ui.*
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.globalpages.repositories.DummyDataRepositories
import com.brainsocket.globalpages.viewModel.BusinessGuideAddViewHolder
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import javax.inject.Inject

import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.schibstedspain.leku.LocationPickerActivity
import java.io.File

class BusinessGuideAddActivity : BaseActivity(), TagsCollectionContact.View, AttachmentContract.View
        , BusinessGuidesContract.View, OnCategorySelectListener {

    companion object {
        var PLACE_PICKER_REQUEST = 1
        var PICTURE_REQUEST = 100

        var MAP_BUTTON_REQUEST_CODE = 101
    }

    @BindView(R.id.businessImages)
    lateinit var businessImages: RecyclerView

    @BindView(R.id.businessTypes)
    lateinit var businessCategories: RecyclerView

    @BindView(R.id.businessSubCategories)
    lateinit var businessSubCategories: RecyclerView

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @Inject
    lateinit var presenter: TagsCollectionPresenter

    @Inject
    lateinit var attachmentPresenter: AttachmentPresenter

    @Inject
    lateinit var businessGuidesPresenter: BusinessGuidesPresenter

    lateinit var businessGuideAddViewHolder: BusinessGuideAddViewHolder


    private fun initDI() {
        val component = DaggerBusinessGuideAddComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .businessGuidesModule(BusinessGuidesModule(this))
                .attachmentModule(AttachmentModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadBusinessCategories(true)

//        businessGuidesPresenter.attachView(this)
//        businessGuidesPresenter.subscribe()


        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()

        businessGuidesPresenter.attachView(this)
        businessGuidesPresenter.subscribe()

    }

    private fun initRecyclerView() {
        businessImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())

        businessCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        businessTypes.adapter = CategoryRecyclerViewAdapter(this, DummyDataRepositories.getCategoriesList())

        businessSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(this, DummyDataRepositories.getSubCategoriesList())

    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_add_layout)
        ButterKnife.bind(this)
        initRecyclerView()
        initDI()
        businessGuideAddViewHolder = BusinessGuideAddViewHolder(findViewById(android.R.id.content))
    }

    @OnClick(R.id.locationEditText)
    fun onLocationEditTextClick(view: View) {
        val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withGeolocApiKey(resources.getString(R.string.google_maps_key))
                .withSearchZone("es_ES")
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .build(applicationContext)

        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)

//        val builder = PlacePicker.IntentBuilder()
//        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        Log.v("", "")
    }

    @OnClick(R.id.addAttachmentBtn)
    fun onAddAttachmentClick(view: View) {
        Pix.start(this@BusinessGuideAddActivity, PICTURE_REQUEST, 1)
//        (businessImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment())
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.skipBtn)
    fun onSkipClick(view: View) {
        finish()
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.businessAddBtn)
    fun onBusinessAddBtn(view: View) {
        if (businessGuideAddViewHolder.isValid()) {
            businessGuidesPresenter.addBusinessGuide(businessGuideAddViewHolder.getBusinessGuideModel())
        }
        Log.v("View Clicked", view.id.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PLACE_PICKER_REQUEST -> {
                    val place = PlacePicker.getPlace(this, data)
                    val toastMsg = String.format("Place: %s", place.name)
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
                }
                PICTURE_REQUEST -> {
                    val returnValue = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    attachmentPresenter.loadAttachmentFile(File(returnValue[0]))
                    Log.v("", "")
                }
                MAP_BUTTON_REQUEST_CODE -> {
                    Log.v("", "")
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
                    Pix.start(this@BusinessGuideAddActivity, PICTURE_REQUEST, 1)
                } else {
                    Toast.makeText(this@BusinessGuideAddActivity, resources.getString(R.string.Approve_Permissions_To_Pick_Images), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    /*Tag Collection presenter started*/
    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
        super.onBusinessCategoriesLoaded(categoriesList)
        businessCategories.adapter = CategoryRecyclerViewAdapter(context = this,
                categoriesList = categoriesList.toMutableList(), onCategorySelectListener = this)
        Log.v("", "")
    }

    override fun showBusinessCategoriesProgress(show: Boolean) {
        super.showBusinessCategoriesProgress(show)
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesLoadErrorMessage(visible: Boolean) {
        super.showBusinessCategoriesLoadErrorMessage(visible)
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesEmptyView(visible: Boolean) {
        super.showBusinessCategoriesEmptyView(visible)
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }
    /*Tag collection presenter ended*/


    /*Attachment presenter started*/
    override fun showAttachmentProgress(show: Boolean) {

        Log.v("", "")
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {
        Log.v("", "")
    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {
        Log.v("", "")
    }

    override fun onLoadAttachmentListSuccessfully(filePath: String) {
        (businessImages.adapter as AttachmentRecyclerViewAdapter).addItem(Attachment(filePath))
        Log.v("", "")
    }
    /*Attachment presenter ended*/

    /*Business guide presenter started*/
    override fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {
        Log.v("", "")
    }

    override fun onAddBusinessGuideSuccessfully() {
        Log.v("", "")
    }

    /*Business guide presenter ended*/

    override fun onSelectCategory(category: Category) {
        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(context = this, subCategoriesList = category.subCategoriesList)
        Log.v("", "")
    }


}