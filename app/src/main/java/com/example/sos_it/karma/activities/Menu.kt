package com.example.sos_it.karma.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.Med
import com.example.sos_it.karma.adapter.MedAdapter
import com.google.firebase.database.*

class Menu : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<Med>
    private lateinit var addr: String
    private lateinit var resname: String
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val progressbar = findViewById<ProgressBar>(R.id.progressBar4)
        Handler().postDelayed({
            progressbar.visibility = View.GONE
        }, 3000)
        userRecyclerView = findViewById(R.id.menulist)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf()
        val path = intent.getStringExtra("Path")
        addr = intent.getStringExtra("restaddr").toString()
        resname = intent.getStringExtra("restname").toString()
        getMenuData(path.toString())
        val messageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val test = intent?.getStringExtra("Item")
                val price = intent?.getStringExtra("Price")
                val tvprice = findViewById<TextView>(R.id.fprice)
                val bab = findViewById<CardView>(R.id.appbar)
                val transition = Slide(Gravity.START).setDuration(600).addTarget(bab)
                TransitionManager.beginDelayedTransition(bab.parent as ViewGroup?, transition)
                bab.visibility = View.VISIBLE
                if (test.toString() == "0") {
                    bab.visibility = View.INVISIBLE
                }
                val total = findViewById<TextView>(R.id.fitem)
                var itemcount = test.toString() + " ITEMS"
                if (test.toString() == "1") {
                    itemcount = test.toString() + " ITEM"
                }
                total.text = itemcount
                val formattedprice = String.format("%.2f", price?.toFloat())
                ("\u20B9 $formattedprice").also { tvprice.text = it }
                bab.setOnClickListener(View.OnClickListener {
                    val cartintent = Intent(context, Cart::class.java)
                    cartintent.putExtra("Price", formattedprice.toFloat())
                    cartintent.putExtra("Rest", addr)
                    cartintent.putExtra("restname", resname)
                    startActivity(cartintent)
                })


            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(messageReceiver, IntentFilter("Cart"))
    }

    private fun getMenuData(path: String) {
        dbref = FirebaseDatabase.getInstance().getReference(path)
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Med::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerView.adapter = MedAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}