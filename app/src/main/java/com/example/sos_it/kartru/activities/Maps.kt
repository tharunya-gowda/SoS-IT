package com.example.sos_it.kartru.activities

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sos_it.R
import com.example.sos_it.kartru.model.WorkItem
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*


class Maps : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnPoiClickListener{
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var path:ArrayList<LatLng>
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        path= arrayListOf()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        var markerOptions:MarkerOptions
        var m:Marker
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
        task.addOnSuccessListener {
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
                        markerOptions= MarkerOptions().position(LatLng(location.latitude,location.longitude)).title("On the Way").draggable(true)
                        val bitmap=BitmapFactory.decodeResource(this.resources, R.drawable.bike)
                        val smallMarker=Bitmap.createScaledBitmap(bitmap,400,150,false)
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        m=googleMap.addMarker(markerOptions)!!
                        val dbref=FirebaseDatabase.getInstance().getReference("Orders")
                        dbref.addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
                                if(snapshot.exists()){
                                    for(item in snapshot.children){
                                        val order=item.getValue(WorkItem::class.java)
                                        if(order?.user==uid) {
                                            m = googleMap.addMarker(
                                                MarkerOptions().position(
                                                    LatLng(
                                                        12.9032951,
                                                        77.5058144
                                                    )
                                                ).draggable(true)
                                            )!!
                                            val context = GeoApiContext.Builder()
                                                .apiKey("AIzaSyAA6gR27aU5sJt-2z349Uzeo1_RVnJB31I")
                                                .build()
                                            val req: DirectionsApiRequest =
                                                DirectionsApi.getDirections(
                                                    context,
                                                    "${location.latitude}+${location.longitude}",
                                                    "12.9032951,77.5058144"
                                                )
                                                val res: DirectionsResult = req.await()
                                                if (res.routes != null && res.routes.isNotEmpty()) {
                                                    val route: DirectionsRoute = res.routes[0]
                                                    if (route.legs != null) {
                                                        for (i in route.legs.indices) {
                                                            val legs: DirectionsLeg = route.legs[i]
                                                            if (legs.steps != null) {
                                                                for (j in legs.steps.indices) {
                                                                    val step: DirectionsStep =
                                                                        legs.steps[j]
                                                                    if (step.steps != null && step.steps.isNotEmpty()) {
                                                                        for (k in step.steps.indices) {
                                                                            val step1: DirectionsStep =
                                                                                step.steps[k]
                                                                            val points1: EncodedPolyline =
                                                                                step1.polyline
                                                                            if (points1 != null) {
                                                                                val coords1 =
                                                                                    points1.decodePath()
                                                                                for (coord1 in coords1) {
                                                                                    path.add(
                                                                                        LatLng(
                                                                                            coord1.lat,
                                                                                            coord1.lng
                                                                                        )
                                                                                    )
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        val points: EncodedPolyline =
                                                                            step.polyline
                                                                        if (points != null) {
                                                                            val coords: List<com.google.maps.model.LatLng> =
                                                                                points.decodePath()
                                                                            for (coord in coords) {
                                                                                path.add(
                                                                                    LatLng(
                                                                                        coord.lat,
                                                                                        coord.lng
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            if (path.size > 0) {
                                                val opts =
                                                    PolylineOptions().addAll(path).color(Color.BLUE)
                                                        .width(5f)
                                                googleMap.addPolyline(opts)
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                        GoogleMapOptions().zoomControlsEnabled(true).zoomGesturesEnabled(true).tiltGesturesEnabled(true).rotateGesturesEnabled(true)
                        googleMap.uiSettings.setAllGesturesEnabled(true)
                        googleMap.isMyLocationEnabled = true
                        googleMap.uiSettings.isMyLocationButtonEnabled=true
                        googleMap.uiSettings.isMapToolbarEnabled=true
                        googleMap.uiSettings.isZoomControlsEnabled=true
                        googleMap.setOnPoiClickListener {
                            m.position=LatLng(it.latLng.latitude,it.latLng.longitude)
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