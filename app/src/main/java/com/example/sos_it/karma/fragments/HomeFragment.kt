package com.example.sos_it.karma.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.*
import com.example.sos_it.karma.activities.*
import com.example.sos_it.karma.adapter.HomeAdapter
import com.example.sos_it.karma.adapter.ResAdapter
import com.example.sos_it.karma.model.HomeItem
import com.example.sos_it.karma.model.Rest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var homelist: ArrayList<HomeItem>
    private lateinit var homerecycler: RecyclerView
    private lateinit var popularlist: ArrayList<Rest>
    private lateinit var popularrecycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val restaurant = view.findViewById<ImageView>(R.id.restaurant)
        restaurant.setOnClickListener {
            startActivity(Intent(context, Restaurants::class.java))
        }
        val pharma = view.findViewById<ImageView>(R.id.pharma)
        pharma.setOnClickListener {
            startActivity(Intent(context, Pharma::class.java))
        }
        val pets = view.findViewById<ImageView>(R.id.petsupplies)
        pets.setOnClickListener {
            startActivity(Intent(context, Pets::class.java))
        }
        val groceries = view.findViewById<ImageView>(R.id.groceries)
        groceries.setOnClickListener {
            val intent = Intent(context, Medicines::class.java)
            intent.putExtra("Path", "Groceries")
            startActivity(intent)
        }
        val services = view.findViewById<ImageView>(R.id.services)
        services.setOnClickListener {
            startActivity(Intent(context, Services::class.java))
        }
        homelist = arrayListOf()
        homerecycler = view.findViewById(R.id.topcurration)
        homerecycler.layoutManager = GridLayoutManager(context, 3)
        getHomeData()
        popularlist = arrayListOf()
        popularrecycler = view.findViewById(R.id.popular)
        popularrecycler.layoutManager = LinearLayoutManager(context)
        getPopular()
        return view
    }

    private fun getPopular() {
        val dbref = FirebaseDatabase.getInstance().getReference("Restaurants")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Rest::class.java)
                        if (user?.popular == "True")
                            popularlist.add(user)
                    }
                    popularrecycler.adapter = ResAdapter(popularlist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getHomeData() {
        val dbref = FirebaseDatabase.getInstance().getReference("Top Curations")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(HomeItem::class.java)
                        homelist.add(user!!)
                    }
                    homerecycler.adapter = HomeAdapter(homelist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

