package com.almersal.android.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.almersal.android.R
import com.almersal.android.data.entities.BusinessGuide
import com.almersal.android.data.entities.PointEntity
import com.almersal.android.dialogs.bottomSheetFragments.BusinessGuideSnippetBottomFragment
import com.almersal.android.eventsBus.EventActions
import com.almersal.android.eventsBus.MessageEvent
import com.almersal.android.eventsBus.RxBus
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
import com.google.gson.Gson

/**
 * Created by Adhamkh on 2018-10-21.
 */
class LocationViewerActivity : BaseActivity(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    companion object {
        const val LocationViewerActivity_Point_Tag = "LocationViewerActivity_Point_Tag"
    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.toolbarTitle)
    lateinit var toolbarTitle: TextView

    lateinit var mMap: GoogleMap

    lateinit var point: PointEntity

    private fun initToolBar() {
        toolbar.setTitle(R.string.Address)
        toolbarTitle.setText(R.string.Address)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun placeMarkerOnMap(pointEntity: PointEntity) {
        val location = LatLng(pointEntity.lat, pointEntity.lng)
        val markerOptions = MarkerOptions().position(location)
        markerOptions.title(resources.getString(R.string.Address))
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_yellow))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
        mMap.addMarker(markerOptions)
    }


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.pickuplocation_layout)
        ButterKnife.bind(this)

        initToolBar()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val jSon = intent.getStringExtra(LocationViewerActivity_Point_Tag)
        point = Gson().fromJson(jSon, PointEntity::class.java)


    }

    override fun onMapClick(p0: LatLng?) {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        placeMarkerOnMap(point)

    }


    override fun onMarkerClick(p0: Marker?): Boolean {
//        if (p0 != null) {
//            if (p0.tag is BusinessGuide) {
//                val businessGuideSnippetBottomFragment = BusinessGuideSnippetBottomFragment.getNewInstance(p0.tag as BusinessGuide)
//                businessGuideSnippetBottomFragment.show(supportFragmentManager,
//                        BusinessGuideSnippetBottomFragment.BusinessGuideSnippetBottomFragment_Tag)
//            }
//        }
        return false
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

}
