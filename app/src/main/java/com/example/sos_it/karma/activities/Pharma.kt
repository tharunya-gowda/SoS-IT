package com.example.sos_it.karma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.cardview.widget.CardView
import com.example.sos_it.R

class Pharma : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharma)
        val firstaid=findViewById<CardView>(R.id.firstaid)
        firstaid.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/First AId")
            startActivity(intent)
        })
        val coldrelief=findViewById<CardView>(R.id.coldrelief)
        coldrelief.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/Cold Relief")
            startActivity(intent)
        })
        val digestives=findViewById<CardView>(R.id.digestives)
        digestives.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/Digestives")
            startActivity(intent)
        })
        val painrelief=findViewById<CardView>(R.id.painrelief)
        painrelief.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/Pain Relief")
            startActivity(intent)
        })
        val skincare=findViewById<CardView>(R.id.skin)
        skincare.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/Skin Care")
            startActivity(intent)
        })
        val jointcare=findViewById<CardView>(R.id.joint)
        jointcare.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/Joint Care")
            startActivity(intent)
        })
        val healthcare=findViewById<CardView>(R.id.healthcare)
        healthcare.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pharma/HealthCare Devices")
            startActivity(intent)
        })
        val presc=findViewById<CardView>(R.id.prescribed)
        presc.setOnClickListener(View.OnClickListener {
            val intent=Intent(this, Prescription::class.java)
            startActivity(intent)
        })
    }
}