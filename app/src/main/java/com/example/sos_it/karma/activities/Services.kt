package com.example.sos_it.karma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sos_it.R
import com.example.sos_it.karma.adapter.ServiceAdapter
import com.example.sos_it.karma.model.Service
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_services.*

class Services : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userArrayList: ArrayList<Service>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        addservice.setOnClickListener {
            startActivity(Intent(this, AddServices::class.java))
        }
        servicerv.layoutManager=LinearLayoutManager(this)
        servicerv.setHasFixedSize(true)
        userArrayList= arrayListOf()
        getServices()
    }

    private fun getServices() {
        dbref= FirebaseDatabase.getInstance().getReference("Services")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userArrayList.clear()
                    for(userSnapshot in snapshot.children){
                        val user=userSnapshot.getValue(Service::class.java)
                        userArrayList.add(user!!)
                    }
                    servicerv.adapter= ServiceAdapter(userArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}