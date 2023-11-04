package com.example.sos_it.main


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.sos_it.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomePage : AppCompatActivity() {
    private var isSwitchOn = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val switchbutton = findViewById<LottieAnimationView>(R.id.switch1)
        val tick = findViewById<ImageButton>(R.id.tick)
        switchbutton.setOnClickListener {
            if (isSwitchOn) {
                switchbutton.setMinAndMaxProgress(0.0f, 0.5f)
                switchbutton.playAnimation()
            } else {
                switchbutton.setMinAndMaxProgress(0.5f, 1.0f)
                switchbutton.playAnimation()
            }
            isSwitchOn = !isSwitchOn

        }
        tick.setOnClickListener {
            if (isSwitchOn) {
                val intent = Intent(this, Kartru::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Karma::class.java)
                startActivity(intent)
            }
        }
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
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latLng =
                            location.latitude.toString() + "," + location.longitude.toString()
                        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        FirebaseDatabase.getInstance().getReference("Delivery/$uid").child("loc")
                            .setValue(latLng)
                    }
                }
        }
    }


    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_help)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("No", null).show()
    }
}