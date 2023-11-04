package com.example.sos_it.karma.activities

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.sos_it.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Addresses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addresses)
        val lat=intent.getStringExtra("Lat").toString()
        val long=intent.getStringExtra("Long").toString()
        val rest=intent.getStringExtra("Rest").toString()
        val resname=intent.getStringExtra("restname").toString()
        val latLng= "$lat,$long"
        val geocoder=Geocoder(this).getFromLocation(lat.toDouble(),long.toDouble(),1)
        var address=geocoder[0]?.subLocality.toString()
        address+=","+geocoder[0]?.locality.toString()
        address+=","+geocoder[0]?.subAdminArea.toString()
        address+=","+geocoder[0]?.adminArea.toString()
        address+=","+geocoder[0]?.postalCode.toString()
        val yourloc=findViewById<TextView>(R.id.yourloc)
        yourloc.text=address
        val et=findViewById<EditText>(R.id.completeaddress)
        val fab=findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener(View.OnClickListener {
            val final=et.text.toString()+","+address
            val next=Intent(this, Cart::class.java)
            next.putExtra("latLng",latLng)
            next.putExtra("addr",final)
            next.putExtra("Rest",rest)
            next.putExtra("restname",resname)
            startActivity(next)
        })
        val change=findViewById<TextView>(R.id.change)
        val close=findViewById<ImageButton>(R.id.close)
        change.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Homemaps::class.java))
        })
        close.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Homemaps::class.java))
        })

    }
}