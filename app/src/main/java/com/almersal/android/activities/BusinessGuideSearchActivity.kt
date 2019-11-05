package com.almersal.android.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.almersal.android.R
import com.almersal.android.adapters.BusinessGuideRecyclerViewAdapter
import com.almersal.android.adapters.TagsRecyclerViewAdapter
import com.almersal.android.data.entities.*
import com.almersal.android.data.filtration.FilterEntity
import com.almersal.android.di.component.DaggerBusinessGuideSearchComponent
import com.almersal.android.di.module.BusinessGuidesModule
import com.almersal.android.di.module.TagsCollectionModule
import com.almersal.android.di.ui.BusinessGuidesContract
import com.almersal.android.di.ui.BusinessGuidesPresenter
import com.almersal.android.di.ui.TagsCollectionContact
import com.almersal.android.di.ui.TagsCollectionPresenter
import com.almersal.android.dialogs.bottomSheetFragments.BusinessGuideSnippetBottomFragment
import com.almersal.android.dialogs.bottomSheetFragments.SubCategoryBottomSheet
import com.almersal.android.enums.FilterType
import com.almersal.android.enums.TagType
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
import com.almersal.android.listeners.OnCategorySelectListener
import com.almersal.android.listeners.OnTagSelectListener
import com.almersal.android.repositories.DummyDataRepositories
import com.almersal.android.utilities.IntentHelper
import com.almersal.android.views.SelectedTagsView
import com.brainsocket.mainlibrary.SupportViews.RecyclerViewDecoration.GridDividerDecoration
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import javax.inject.Inject


class BusinessGuideSearchActivity : BaseActivity(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
    BusinessGuidesContract.View, TagsCollectionContact.View, OnTagSelectListener, OnCategorySelectListener,
    GoogleMap.OnCameraIdleListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val PLACE_PICKER_REQUEST = 3
    }

    private lateinit var locationCallback: LocationCallback

    private lateinit var locationRequest: LocationRequest

    private var lastLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var locationUpdateState: Boolean = false

    @Inject
    lateinit var businessGuidesPresenter: BusinessGuidesPresenter

    @Inject
    lateinit var tagCollectionPresenter: TagsCollectionPresenter

    @BindView(R.id.businessGuideRecyclerView)
    lateinit var businessGuideRecyclerView: RecyclerView

    lateinit var lm: LinearLayoutManager

    @BindView(R.id.businessGuideRecyclerViewContainer)
    lateinit var businessGuideRecyclerViewContainer: View

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.refreshBtn)
    lateinit var refreshBtn: View

    @BindView(R.id.selectedTagsView)
    lateinit var selectedTagsView: SelectedTagsView


    lateinit var mMap: GoogleMap
    var firstLocation = true
    var firstFilterEntity: FilterEntity? = null
    var limit = 100
    var pageId = 0
    var maxDistance = 0f
    var changeViewTypeFlag = true
    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView() {

        lm = LinearLayoutManager(this)
        businessGuideRecyclerView.layoutManager = lm
        businessGuideRecyclerView.addItemDecoration(GridDividerDecoration(this))
        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this, mutableListOf())

        businessGuideRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                val visibleItemCount = lm.childCount
                val totalItemCount = lm.itemCount
                val firstVisibleItemPosition = lm.findFirstVisibleItemPosition()
                if (progressBar.visibility == View.GONE)
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= limit
                    ) {
                        pageId++
                        businessGuidesPresenter.loadBusinessGuideByLocation(
                            pointEntity =
                            PointEntity(lat = lastLocation!!.latitude, lng = lastLocation!!.longitude),
                            limit = limit,
                            skip = limit * pageId, filterEntity = firstFilterEntity, maxDistance = maxDistance

                        )
                    }
            }
        })
        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(this, DummyDataRepositories.getTagsDefaultRepositories()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this

    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0?.lastLocation != null) {

                    if (firstLocation) {
                        firstLocation = false
                        lastLocation = p0.lastLocation
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastLocation!!.latitude,
                                    lastLocation!!.longitude
                                ), 12.0f
                            )
                        )

                    }
                }


            }
        }

    }

    private fun initDI() {
        val component = DaggerBusinessGuideSearchComponent.builder()
            .tagsCollectionModule(TagsCollectionModule(this))
            .businessGuidesModule(BusinessGuidesModule(this))
            .build()
        component.inject(this)

        businessGuidesPresenter.attachView(this)

        tagCollectionPresenter.attachView(this)
        tagCollectionPresenter.loadBusinessCategories(true)

    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
//        mMap.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
//                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }


    /*GPS Track Started */
    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this@BusinessGuideSearchActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /*GPS Track Ended */


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.business_guide_search_layout)
        ButterKnife.bind(this)

        initToolBar()

        initRecyclerView()

        initDI()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initLocation()
        createLocationRequest()

        RxBus.listen(MessageEvent::class.java).subscribe {
            when (it.action) {
                EventActions.Business_Filter_Activity_Tag -> {
                    setFilterEntity(it.message as FilterEntity)
                }
            }
        }


    }

    private fun setFilterEntity(filterEntity: FilterEntity) {

//__________________Offline Filtering_____________________


//        if (businessGuideRecyclerView.adapter != null) {
//            val adapter = businessGuideRecyclerView.adapter as BusinessGuideRecyclerViewAdapter
//            adapter.filterByCriteria(filterEntity)
//            val businessGuideList = adapter.businessGuideList
//
//            mMap.clear()
//            if (lastLocation != null)
//                placeMarkerOnMap(LatLng(lastLocation!!.latitude, lastLocation!!.longitude))
//            businessGuideList.forEach {
//                addMarker(it, it.getName(), it.locationPoint)
//            }
//        }

        selectedTagsView.setAdapter(TagsRecyclerViewAdapter(baseContext, filterEntity.getTags()))
        (selectedTagsView.getAdapter() as TagsRecyclerViewAdapter).onTagSelectListener = this
        pageId = 0
        firstFilterEntity = filterEntity
        businessGuidesPresenter.loadBusinessGuideByLocation(
            pointEntity =
            PointEntity(lat = lastLocation!!.latitude, lng = lastLocation!!.longitude),
            limit = limit,
            skip = limit * pageId, filterEntity = firstFilterEntity, maxDistance = maxDistance

        )


    }


    /*Tag filter started*/
    @OnClick(R.id.selectedTagsView)
    fun onTagSearchViewClick(view: View) {
        try {
            val fragment = supportFragmentManager.findFragmentByTag(SubCategoryBottomSheet.SubCategoryBottomSheet_Tag)
            if (fragment != null) {
                (fragment as SubCategoryBottomSheet).dismiss()
            }
        } catch (ex: Exception) {
        }
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.searchBtn)
    fun onSearchBtnClick(view: View) {
        val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(
            activity = this,
            tagList = list,
            filter = FilterType.BusinessFilter
        )
        Log.v("View Clicked", view.id.toString())
    }

    override fun onSelectTag(tagEntity: TagEntity) {

//            if (adapter.filterEntity != null)
        when (tagEntity.tagType) {
            TagType.Category -> {
                firstFilterEntity!!.category = null
            }
            TagType.SubCategory -> {
                firstFilterEntity!!.subCategory = null
            }
            TagType.City -> {
                firstFilterEntity!!.city = null
            }
            TagType.Location -> {
                firstFilterEntity!!.area = null
            }
            TagType.Query -> {
                firstFilterEntity!!.query = null
            }
        }
        firstFilterEntity?.let { setFilterEntity(it) }

    }

    override fun onTagClick(tagEntity: TagEntity) {
        val list = (selectedTagsView.selectedTags.adapter as TagsRecyclerViewAdapter).tagsListList
        IntentHelper.startPostSearchFilterActivityForResult(
            activity = this,
            tagList = list, filter = FilterType.BusinessFilter
        )
    }
    /*Tag filter ended*/

    @OnClick(R.id.refreshBtn)
    fun onRefreshButtonClick(view: View) {
        if (lastLocation != null) {
            businessGuidesPresenter.loadBusinessGuideByLocation(
                pointEntity =
                PointEntity(lat = lastLocation!!.latitude, lng = lastLocation!!.longitude),
                limit = 100,
                skip = 0,
                filterEntity = firstFilterEntity,
                maxDistance = maxDistance
            )
        }
    }

    @OnCheckedChanged(R.id.viewTypeToggle)
    fun onViewTypeSelected(button: CompoundButton, checked: Boolean) {
        if (checked) {
            businessGuideRecyclerView.visibility = View.VISIBLE

            businessGuideRecyclerViewContainer.visibility = View.VISIBLE
//            businessGuideRecyclerViewContainer.alpha=0.3f
        } else {
            businessGuideRecyclerView.visibility = View.GONE

            businessGuideRecyclerViewContainer.visibility = View.GONE
        }
        Log.v("View Clicked", button.id.toString())
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.setOnMarkerClickListener(this)
//        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraIdleListener(this)
        setUpMap()
    }

    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                var addressText = place.name.toString()
                addressText += "\n" + place.address.toString()

//                placeMarkerOnMap(place.latLng)
            }
        }
    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
    /*GPS Track Ended*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        createLocationRequest()
        Log.v("", "")

    }

    override fun onCameraIdle() {
        lastLocation?.latitude = mMap.projection.visibleRegion.latLngBounds.center.latitude
        lastLocation?.longitude = mMap.projection.visibleRegion.latLngBounds.center.longitude
        val currentLatLng = LatLng(lastLocation?.latitude!!, lastLocation?.longitude!!)
        //placeMarkerOnMap(currentLatLng)
        val distance: FloatArray? = floatArrayOf(0f, 0f, 0f)
        Location.distanceBetween(
            mMap.projection.visibleRegion.latLngBounds.northeast.latitude,
            mMap.projection.visibleRegion.latLngBounds.northeast.longitude,
            mMap.cameraPosition.target.latitude,
            mMap.cameraPosition.target.longitude,
            distance
        )

        maxDistance = distance!![0] / 1000
        pageId = 0
        businessGuidesPresenter.loadBusinessGuideByLocation(
            pointEntity =
            PointEntity(lat = lastLocation!!.latitude, lng = lastLocation!!.longitude),
            limit = limit,
            skip = limit * pageId, filterEntity = firstFilterEntity, maxDistance = maxDistance
        )
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null) {
            if (p0.tag is BusinessGuide) {
                Handler().postDelayed({
                    val businessGuideSnippetBottomFragment =
                        BusinessGuideSnippetBottomFragment.getNewInstance(p0.tag as BusinessGuide)
                    businessGuideSnippetBottomFragment.show(
                        supportFragmentManager,
                        BusinessGuideSnippetBottomFragment.BusinessGuideSnippetBottomFragment_Tag
                    )
                }, 200)
            }
        }
        return false
    }

    /*Business Guides Presenter started*/

    override fun showBusinessGuideProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            refreshBtn.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            refreshBtn.visibility = View.GONE
        }
    }

    override fun showBusinessGuideLoadErrorMessage(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.GONE
            refreshBtn.visibility = View.VISIBLE
            Toast.makeText(baseContext, R.string.NoInternetConnectionTryRefreshData, Toast.LENGTH_LONG).show()
        } else {
            progressBar.visibility = View.GONE
            refreshBtn.visibility = View.GONE
        }
    }

    override fun showBusinessGuideEmptyView(visible: Boolean) {
        if (pageId == 0) {
            businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this, mutableListOf())
            mMap.clear()
            Toast.makeText(baseContext, R.string.NoDataFound, Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {

        if (pageId == 0) {
            businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this, businessGuideList)
            mMap.clear()
        } else
            (businessGuideRecyclerView.adapter as BusinessGuideRecyclerViewAdapter).addAll(businessGuideList)
        var viewTypeToggle = findViewById<CompoundButton>(R.id.viewTypeToggle)
        if (changeViewTypeFlag) {
            changeViewTypeFlag = false
            viewTypeToggle.isChecked = true
        }
        businessGuideList.forEach {
            addMarker(it, it.getName(), it.locationPoint)
        }
//        if (firstFilterEntity != null)
//            setFilterEntity(firstFilterEntity!!)
        Log.v("", "")
    }

    private fun addMarker(businessGuide: BusinessGuide, title: String, pointEntity: PointEntity) {
        try {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(pointEntity.lat, pointEntity.lng))
                    .title(title)
                    .icon(
                        BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    )
            )
            marker.tag = businessGuide
        } catch (ex: Exception) {
            Log.v("", "'")
        }

    }

    /*Business Guides Presenter ended*/


    /*Tags Presenter started*/
    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
//        val categoryFilterBottomSheet = CategoryFilterBottomSheet.getNewInstance(categoriesList.toMutableList(), this)
//        categoryFilterBottomSheet.show(supportFragmentManager, CategoryFilterBottomSheet.CategoryFilterBottomSheet_Tag)
//        Log.v("", "")
    }
    /*Tags Presenter ended*/


    override fun onSelectCategory(category: Category) {
        val filterEntity = FilterEntity()
        filterEntity.category = category
        firstFilterEntity = filterEntity
        setFilterEntity(filterEntity)
        Log.v("", "")
    }

}
