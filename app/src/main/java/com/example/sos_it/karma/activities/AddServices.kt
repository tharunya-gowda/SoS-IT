package com.example.sos_it.karma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sos_it.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_services.*

class AddServices : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_services)
        floatingActionButton2.setOnClickListener {
            val name=servicename.text.toString()
            val addr=serviceaddr.text.toString()
            val phone=servicephone.text.toString()
            val dbref=FirebaseDatabase.getInstance().getReference("Services/$name")
            dbref.child("name").setValue(name)
            dbref.child("address").setValue(addr)
            dbref.child("phone").setValue(phone)
            startActivity(Intent(this, Services::class.java))
        }
    }
}