package com.example.sos_it.karma.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.karma.adapter.CartAdapter
import com.example.sos_it.karma.model.CartItem
import com.example.sos_it.karma.activities.Homemaps
import com.example.sos_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    lateinit var userArrayList: ArrayList<CartItem>
    private var finalprice=0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view:View=  inflater.inflate(R.layout.fragment_cart, container, false)
        val loc=view.findViewById<TextView>(R.id.locf)
        loc.setOnClickListener {
            startActivity(Intent(context, Homemaps::class.java))
        }
        userRecyclerView=view.findViewById(R.id.frcartlist)
        userRecyclerView.layoutManager= LinearLayoutManager(context)
        userRecyclerView.setHasFixedSize(true)
        userArrayList=arrayListOf()
        val pricetag=view.findViewById<CardView>(R.id.pricetagf)
        pricetag.visibility=View.INVISIBLE
        getCartData()
        return view
    }

    private fun getCartData() {
        val uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref= FirebaseDatabase.getInstance().getReference("Users/$uid/Cart")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val pricetag=view?.findViewById<CardView>(R.id.pricetag)
                    pricetag?.visibility=View.VISIBLE
                    userArrayList.clear()
                    finalprice=0.0f
                    for(userSnapshot in snapshot.children){
                        val user=userSnapshot.getValue(CartItem::class.java)
                        val price= user?.itemprice?.substring(3)?.toFloat()
                        val count=user?.itemcount?.toInt()
                        if(price!=null && count!=null) {
                            finalprice += price?.times(count!!)
                        }
                        userArrayList.add(user!!)
                    }
                    if(finalprice==0.0f) {
                        pricetag?.visibility=View.INVISIBLE
                    }
                    val finalpr= view?.findViewById<TextView>(R.id.finalprice)
                    val final="\u20B9"+finalprice.toString()
                    finalpr?.text=final
                    userRecyclerView.adapter= CartAdapter(userArrayList)
                }
                else{
                    userArrayList.clear()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

}