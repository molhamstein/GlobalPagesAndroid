package com.brainsocket.globalpages.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
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
import com.brainsocket.globalpages.R
import com.brainsocket.globalpages.adapters.BusinessGuideRecyclerViewAdapter
import com.brainsocket.globalpages.data.entities.*
import com.brainsocket.globalpages.di.component.DaggerFindNearByComponent
import com.brainsocket.globalpages.di.module.BusinessGuidesModule
import com.brainsocket.globalpages.di.module.TagsCollectionModule
import com.brainsocket.globalpages.di.ui.BusinessGuidesContract
import com.brainsocket.globalpages.di.ui.BusinessGuidesPresenter
import com.brainsocket.globalpages.di.ui.TagsCollectionContact
import com.brainsocket.globalpages.di.ui.TagsCollectionPresenter
import com.brainsocket.globalpages.dialogs.bottomSheetFragments.BusinessGuideSnippetBottomFragment
import com.brainsocket.globalpages.dialogs.bottomSheetFragments.CategoryFilterBottomSheet
import com.brainsocket.globalpages.dialogs.bottomSheetFragments.SubCategoryBottomSheet
import com.brainsocket.globalpages.listeners.OnCategorySelectListener
import com.brainsocket.globalpages.listeners.OnSubCategorySelectListener
import com.brainsocket.mainlibrary.SupportViews.RecyclerViewDecoration.GridDividerDecoration
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import javax.inject.Inject


class FindNearByActivity : BaseActivity(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        TagsCollectionContact.View, BusinessGuidesContract.View, OnSubCategorySelectListener, OnCategorySelectListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val PLACE_PICKER_REQUEST = 3
    }

    private lateinit var locationCallback: LocationCallback

    private lateinit var locationRequest: LocationRequest

    private lateinit var lastLocation: Location

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationUpdateState: Boolean = false

    @BindView(R.id.businessGuideRecyclerView)
    lateinit var businessGuideRecyclerView: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.refreshBtn)
    lateinit var refreshBtn: View

//    @BindView(R.id.tagSearchView)
//    lateinit var tagSearchView: TagSearchView

    lateinit var mMap: GoogleMap

    var firstLocation = true

    var subCategory: SubCategory? = null


    private fun initToolBar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView() {
        businessGuideRecyclerView.layoutManager = LinearLayoutManager(this)
        businessGuideRecyclerView.addItemDecoration(GridDividerDecoration(this))
    }

    private fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                if (firstLocation)
                    placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }

    }

    private fun placeMarkerOnMap(location: LatLng) {
        firstLocation = false
        val markerOptions = MarkerOptions().position(location)
//        val titleStr = getAddress(location)  // add these two lines
        markerOptions.title(resources.getString(R.string.Your))
//        currentcity = CityModel(titleStr, location, CitiesManager.getCitiesSize() == 0)
//        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
        mMap.addMarker(markerOptions)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
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
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }


    /*GPS Track Started */
    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
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
                    e.startResolutionForResult(this@FindNearByActivity,
                            REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /*GPS Track Ended */


    @Inject
    lateinit var presenter: TagsCollectionPresenter

    @Inject
    lateinit var businessGuidesPresenter: BusinessGuidesPresenter


    private fun initDI() {
        val component = DaggerFindNearByComponent.builder()
                .tagsCollectionModule(TagsCollectionModule(this))
                .businessGuidesModule(BusinessGuidesModule(this))
                .build()
        component.inject(this)

        presenter.attachView(this)
        presenter.subscribe()
        presenter.loadBusinessCategories(true)

        businessGuidesPresenter.attachView(this)
        presenter.subscribe()

    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.find_nearby_layout)
        ButterKnife.bind(this)

        initToolBar()

        initRecyclerView()

        initDI()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this
//                , DummyDataRepositories.getBusinessGuideList())

        initLocation()
        createLocationRequest()


    }

    @OnClick(R.id.tagSearchView)
    fun onTagSearchViewClick(view: View) {
        try {
            var fragment = supportFragmentManager.findFragmentByTag(SubCategoryBottomSheet.SubCategoryBottomSheet_Tag)
            if (fragment != null) {
                (fragment as SubCategoryBottomSheet).dismiss()
            }
        } catch (ex: Exception) {
        }
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.SearchBtn)
    fun onSearchBtnClick(view: View) {
        presenter.loadBusinessCategories(true)
        Log.v("View Clicked", view.id.toString())
    }

    @OnClick(R.id.refreshBtn)
    fun onRefreshButtonClick(view: View) {
        if (subCategory != null) {
            businessGuidesPresenter.loadBusinessGuideList(subCategory = subCategory!!)
        }
    }


    @OnCheckedChanged(R.id.viewTypeToggle)
    fun onViewTypeSelected(button: CompoundButton, checked: Boolean) {
        if (checked) {
            businessGuideRecyclerView.visibility = View.VISIBLE
        } else {
            businessGuideRecyclerView.visibility = View.GONE
        }
        Log.v("View Clicked", button.id.toString())
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
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
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                var addressText = place.name.toString()
                addressText += "\n" + place.address.toString()

                placeMarkerOnMap(place.latLng)
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

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null) {
            if (p0.tag is BusinessGuide) {
                val businessGuideSnippetBottomFragment = BusinessGuideSnippetBottomFragment.getNewInstance(p0.tag as BusinessGuide)
                businessGuideSnippetBottomFragment.show(supportFragmentManager,
                        BusinessGuideSnippetBottomFragment.BusinessGuideSnippetBottomFragment_Tag)
            }
        }
        return false
    }


    /*Tags Presenter started*/
    override fun onBusinessCategoriesLoaded(categoriesList: MutableList<BusinessGuideCategory>) {
        val categoryFilterBottomSheet = CategoryFilterBottomSheet.getNewInstance(categoriesList.toMutableList(), this)
        categoryFilterBottomSheet.show(supportFragmentManager, CategoryFilterBottomSheet.CategoryFilterBottmSheet_Tag)
        Log.v("", "")
    }
    /*Tags Presenter ended*/

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
        mMap.clear()
        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this, Vector())
        Toast.makeText(baseContext, R.string.NoDataFound, Toast.LENGTH_LONG).show()
    }

    override fun onLoadBusinessGuideListSuccessfully(businessGuideList: MutableList<BusinessGuide>) {
        businessGuideRecyclerView.adapter = BusinessGuideRecyclerViewAdapter(this, businessGuideList)
        mMap.clear()
        businessGuideList.forEach {
            addMarker(it, it.getName(), it.locationPoint)
        }
        Log.v("", "")
    }

    private fun addMarker(businessGuide: BusinessGuide, title: String, pointEntity: PointEntity) {
        try {
            val marker = mMap.addMarker(MarkerOptions()
                    .position(LatLng(pointEntity.lat, pointEntity.lng))
                    .title(title)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            marker.tag = businessGuide
        } catch (ex: Exception) {
            Log.v("", "'")
        }

    }

    /*Business Guides Presenter ended*/

    override fun onSelectSubCategory(subCategory: SubCategory) {
        this.subCategory = subCategory
        businessGuidesPresenter.loadBusinessGuideList(subCategory)
    }

    override fun onSelectCategory(category: Category) {
        val subCategoryBottomSheet = SubCategoryBottomSheet.getNewInstance(category.subCategoriesList, this)
        subCategoryBottomSheet.show(supportFragmentManager, SubCategoryBottomSheet.SubCategoryBottomSheet_Tag)
    }
}
