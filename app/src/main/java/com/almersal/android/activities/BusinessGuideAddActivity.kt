package com.almersal.android.activities

import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Optional
import com.almersal.android.R
import com.almersal.android.adapters.*
import com.almersal.android.data.entities.*
import com.almersal.android.data.entitiesModel.BusinessGuideEditModel
import com.almersal.android.di.component.DaggerBusinessGuideAddComponent
import com.almersal.android.di.module.AttachmentModule
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.*
import com.almersal.android.dialogs.OpenDaysDialog
import com.almersal.android.dialogs.ProgressDialog
import com.almersal.android.enums.MediaTypeEnum
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnCitySelectListener
import com.almersal.android.listeners.OnOpenDayListListener
import com.almersal.android.listeners.OnSubCategorySelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.repositories.SettingData
import com.almersal.android.repositories.UserRepository
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.viewModel.BusinessGuideAddViewHolder
import com.brainsocket.mainlibrary.Enums.LayoutStatesEnum
import com.brainsocket.mainlibrary.Listeners.OnRefreshLayoutListener
import com.brainsocket.mainlibrary.Views.Stateslayoutview
import com.github.florent37.viewanimator.ViewAnimator
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.gson.Gson
import java.io.File
import javax.inject.Inject
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil

class BusinessGuideAddActivity : BaseActivity(), TagsCollectionContact.View, AttachmentContract.View
        , BusinessGuidesContract.View, OnCategorySelectListener, OnSubCategorySelectListener
        , OnCitySelectListener, OnOpenDayListListener {

    companion object {

        const val BusinessGuideAddActivity_Tag = "BusinessGuideAddActivity_Tag"

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

    @BindView(R.id.subCategoryLayout)
    lateinit var subCategoryLayout: View

    @BindView(R.id.resultContainer)
    lateinit var resultContainer: View

    @BindView(R.id.baseContainer)
    lateinit var baseContainer: View

    @BindView(R.id.mainStateLayout)
    lateinit var mainStateLayout: Stateslayoutview

    @BindView(R.id.stateLayout)
    lateinit var stateLayout: Stateslayoutview

    @BindView(R.id.businessAddBtn)
    lateinit var businessAddBtn: Button

    @BindView(R.id.cityStateLayout)
    lateinit var cityStateLayout: Stateslayoutview

    @BindView(R.id.adCities)
    lateinit var adCities: RecyclerView

    @BindView(R.id.adLocations)
    lateinit var adLocations: RecyclerView

    @BindView(R.id.areaContainer)
    lateinit var areaContainer: View

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
        presenter.loadCities(withCache = true)

        attachmentPresenter.attachView(this)
        attachmentPresenter.subscribe()

        businessGuidesPresenter.attachView(this)
        businessGuidesPresenter.subscribe()

    }

    private fun initRecyclerView() {
        businessImages.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        businessImages.adapter = AttachmentRecyclerViewAdapter(this, DummyDataRepositories.getAttachmentList())

        businessCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        businessSubCategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        adCities.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        adLocations.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

    }

    private fun animateResult() {

        ViewAnimator.animate(baseContainer).translationY(2000f)
                /*.alpha(1f, 0f).andAnimate(resultContainer).translationY(1000f, 0f)
                .alpha(0f, 1f)*/.start()
    }

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_add_layout)
        ButterKnife.bind(this)
        initRecyclerView()
        initDI()

        businessGuideAddViewHolder = BusinessGuideAddViewHolder(findViewById(android.R.id.content), this)

        val businessGuideString: String? = intent.extras?.getString(BusinessGuideAddActivity_Tag, null)
        if (!businessGuideString.isNullOrEmpty()) {
            val businessGuide: BusinessGuide = Gson().fromJson(businessGuideString, BusinessGuide::class.java)
            businessGuideAddViewHolder.bindBusinessGuide(businessGuide)
            businessAddBtn.setText(R.string.Update)
        }

        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.LocationPickupActivity_Tag -> {
                    if (it.message is PointEntity) {
                        val str = it.message.lat.toString() + ";" + it.message.lng.toString()
                        businessGuideAddViewHolder.locationEditText.setText(str)
                    }
                }
            }
        }

        mainStateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                requestAction()
            }

            override fun onRequestPermission() {

            }
        })

        stateLayout.setOnRefreshLayoutListener(object : OnRefreshLayoutListener {
            override fun onRefresh() {
                presenter.loadBusinessCategories(false)
            }

            override fun onRequestPermission() {

            }
        })

    }

    fun requestAction() {
        if (businessGuideAddViewHolder.isValid()) {

            val token = UserRepository(baseContext).getUser()!!.token
            if (businessGuideAddViewHolder.isAdd())
                businessGuidesPresenter.addBusinessGuide(businessGuideAddViewHolder.getBusinessGuideModel(), token)
            else
                businessGuidesPresenter.updateBusinessGuide(businessGuideAddViewHolder.getBusinessGuideModel()
                        as BusinessGuideEditModel, token)
        }
    }


    @Optional
    @OnClick(R.id.editOpenDaysTextView, R.id.openDayLayout)
    fun onEditOpenDaysTextViewClick(view: View) {
        val openDaysDialog = OpenDaysDialog.newInstance(businessGuideAddViewHolder.openDayList, this)
        openDaysDialog.show(supportFragmentManager, OpenDaysDialog.OpenDaysDialog_Tag)
    }

    @OnClick(R.id.locationEditText)
    fun onLocationEditTextClick(view: View) {
        val canonicalName = LocationPickupActivity::class.java.canonicalName
        IntentHelper.startLocationCheckActivity(context = baseContext, activityName = canonicalName)

//        val locationPickerIntent = LocationPickerActivity.Builder()
//                .withLocation(41.4036299, 2.1743558)
//                .withGeolocApiKey(resources.getString(R.string.google_maps_key))
//                .withSearchZone("es_ES")
//                .shouldReturnOkOnBackPressed()
//                .withStreetHidden()
//                .withCityHidden()
//                .withZipCodeHidden()
//                .withSatelliteViewHidden()
//                .withGooglePlacesEnabled()
//                .withGoogleTimeZoneEnabled()
//                .withVoiceSearchHidden()
//                .build(applicationContext)
//
//        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)

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
        finishAffinity()
        IntentHelper.startMainActivity(this)

        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.businessAddBtn)
    fun onBusinessAddBtn(view: View) {
        requestAction()
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.adBackHomeBtn)
    fun onAdBackHomeButtonClick(view: View) {
        finishAffinity()
        IntentHelper.startMainActivity(this)
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
        adCities.adapter = CityRecyclerViewAdapter(context = baseContext, citiesListList = citiesList,
                onCitySelectListener = this)
    }

    override fun onSelectCity(city: City) {
        if (city.locations.isEmpty()) {
            areaContainer.visibility = View.GONE
        } else {
            areaContainer.visibility = View.VISIBLE
        }
        adLocations.adapter = LocationEntityRecyclerViewAdapter(baseContext, city.locations)
    }

    override fun showBusinessCategoriesProgress(show: Boolean) {
        if (show) {
            stateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesLoadErrorMessage(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun showBusinessCategoriesEmptyView(visible: Boolean) {
        if (visible) {
            stateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            stateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
        Log.v("", "")
    }

    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
        val list = categoriesList.filter { it.parentCategoryId.isNullOrBlank() }.toMutableList()
        businessCategories.adapter = CategoryRecyclerViewAdapter(context = this,
                categoriesList = list.toMutableList(), onCategorySelectListener = this)
        Log.v("", "")
    }

    override fun onSelectCategory(category: Category) {
        if (category.id == SettingData.pharmacyCategoryId) {
            businessGuideAddViewHolder.openDayLayout.visibility = View.VISIBLE
            ViewAnimator.animate(businessGuideAddViewHolder.openDayLabel).textColor(ContextCompat.getColor(this, R.color.grayLightTextColor),
                    ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.grayLightTextColor))
                    .duration(1000).repeatMode(ValueAnimator.REVERSE).start()
        } else {
            businessGuideAddViewHolder.openDayLayout.visibility = View.GONE
        }
        businessSubCategories.adapter = SubCategoryRecyclerViewAdapter(context = this,
                subCategoriesList = category.subCategoriesList, onSubCategorySelectListener = this)
        if (!category.subCategoriesList.isEmpty()) {
            subCategoryLayout.visibility = View.VISIBLE
        } else {
            subCategoryLayout.visibility = View.GONE
        }
        businessGuideAddViewHolder.openDayList = mutableListOf()
        Log.v("", "")
    }

    override fun onSelectSubCategory(subCategory: SubCategory) {
        if (subCategory.id == SettingData.pharmacyCategoryId) {
            businessGuideAddViewHolder.openDayLayout.visibility = View.VISIBLE
            ViewAnimator.animate(businessGuideAddViewHolder.openDayLabel).textColor(ContextCompat.getColor(this, R.color.grayLightTextColor),
                    ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this, R.color.grayLightTextColor))
                    .duration(1000).repeatMode(ValueAnimator.REVERSE).start()
        } else {
            businessGuideAddViewHolder.openDayLayout.visibility = View.GONE
        }
    }

    override fun onUnSelectSubCategory(subCategory: SubCategory) {
        businessGuideAddViewHolder.openDayLayout.visibility = View.GONE
    }

    /*Tag collection presenter ended*/


    /*Attachment presenter started*/
    override fun showAttachmentProgress(show: Boolean) {
        if (show) {
//            val progressDialog: ProgressDialog? =
//                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
//            progressDialog?.dialog?.dismiss()
            val dialog = ProgressDialog.newInstance()
            dialog.showDialog(supportFragmentManager)
        } else {
            ProgressDialog.closeDialog(supportFragmentManager)
//            val progressDialog: ProgressDialog? =
//                    supportFragmentManager.findFragmentByTag(ProgressDialog.ProgressDialog_Tag) as ProgressDialog?
//            progressDialog?.dialog?.dismiss()
        }
        Log.v("", "")
    }

    override fun showAttachmentProcessingPercentage(percentage: String) {
        Log.v("", "")
    }

    override fun showAttachmentLoadErrorMessage(visible: Boolean) {
        if (visible) {
            ProgressDialog.closeDialog(supportFragmentManager)
            Toast.makeText(baseContext, R.string.checkInternetConnection, Toast.LENGTH_LONG).show()
        } else {

        }
        Log.v("", "")
    }

    override fun showAttachmentEmptyView(visible: Boolean) {
        Log.v("", "")
    }

    override fun onLoadAttachmentListSuccessfully(filePath: String, thumbnail: String) {
        (businessImages.adapter as AttachmentRecyclerViewAdapter)
                .addItem(Attachment(filePath, MediaTypeEnum.IMAGES.type, thumbnail))
        Toast.makeText(baseContext, R.string.uploadFileSuccessfully, Toast.LENGTH_LONG).show()
        Log.v("", "")
    }
    /*Attachment presenter ended*/


    /*Business guide presenter started*/
    override fun showBusinessGuideProgress(show: Boolean) {
        if (show) {
            mainStateLayout.FlipLayout(LayoutStatesEnum.Waitinglayout)
        } else {
            mainStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showBusinessGuideLoadErrorMessage(visible: Boolean) {
        if (visible) {
            mainStateLayout.FlipLayout(LayoutStatesEnum.Noconnectionlayout)
        } else {
            mainStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }

    override fun showBusinessGuideEmptyView(visible: Boolean) {
        if (visible) {
            mainStateLayout.FlipLayout(LayoutStatesEnum.Nodatalayout)
        } else {
            mainStateLayout.FlipLayout(LayoutStatesEnum.SuccessLayout)
        }
    }


    override fun onAddBusinessGuideSuccessfully(businessGuide: BusinessGuide) {
        businessGuideAddViewHolder.bindBusinessGuide(businessGuide)
        animateResult()

        Log.v("", "")
    }

    override fun onUpdateBusinessGuideSuccessfully(businessGuide: BusinessGuide) {
        businessGuideAddViewHolder.bindBusinessGuide(businessGuide)
        animateResult()
    }

    /*Business guide presenter ended*/

    override fun onOpenDayListSelect(openDayList: MutableList<String>) {
        businessGuideAddViewHolder.openDayList = openDayList
    }


}