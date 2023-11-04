package com.example.sos_it.karma.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.adapter.ResAdapter
import com.example.sos_it.karma.model.Rest
import com.google.firebase.database.*

class Restaurants : AppCompatActivity() {

    private lateinit var dbref:DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Rest>


    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        val progressbar=findViewById<ProgressBar>(R.id.progressBar2)
        Handler().postDelayed({
            progressbar.visibility= View.GONE
        },3000)
        userRecyclerView=findViewById(R.id.userList)
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList= arrayListOf<Rest>()
        getResData()

    }

    private fun getResData() {

        dbref=FirebaseDatabase.getInstance().getReference("Restaurants")

        dbref.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){

                    for(userSnapshot in snapshot.children){


                        val user=userSnapshot.getValue(Rest::class.java)
                        userArrayList.add(user!!)

                    }

                    userRecyclerView.adapter= ResAdapter(userArrayList)


                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}