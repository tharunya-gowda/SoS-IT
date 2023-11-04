package com.example.sos_it.karma.activities

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.example.sos_it.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Homemaps : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnPoiClickListener{
    private lateinit var mh:Marker
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homemaps)
        val rest=intent.getStringExtra("Rest").toString()
        val resname=intent.getStringExtra("restname").toString()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_home) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val fab=findViewById<FloatingActionButton>(R.id.loc_home)
        fab.setOnClickListener(View.OnClickListener {
            val next=Intent(this, Addresses::class.java)
            next.putExtra("Lat",mh.position.latitude.toString())
            next.putExtra("Long",mh.position.longitude.toString())
            next.putExtra("Rest",rest)
            next.putExtra("restname",resname)
            startActivity(next)
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        var markerOptions:MarkerOptions
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101)
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                            R.raw.style
                        ))
                        googleMap.setOnPoiClickListener(this)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),18f))
                        markerOptions= MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("Your item will be delivered here").draggable(true)
                        mh=googleMap.addMarker(markerOptions)
                        GoogleMapOptions().zoomControlsEnabled(true).zoomGesturesEnabled(true).tiltGesturesEnabled(true).rotateGesturesEnabled(true)
                        googleMap.uiSettings.setAllGesturesEnabled(true)
                        googleMap.isMyLocationEnabled = true
                        googleMap.uiSettings.isCompassEnabled=true
                        googleMap.uiSettings.isIndoorLevelPickerEnabled=true
                        googleMap.uiSettings.isTiltGesturesEnabled=true
                        googleMap.uiSettings.isMyLocationButtonEnabled=true
                        googleMap.uiSettings.isMapToolbarEnabled=true
                        googleMap.uiSettings.isZoomControlsEnabled=true
                        googleMap.setOnPoiClickListener {
                            mh.position=LatLng(it.latLng.latitude,it.latLng.longitude)
                        }




                    }

                }
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                try {
                    exception.startResolutionForResult(this,
                        101)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onPoiClick(poi: PointOfInterest) {

    }
}