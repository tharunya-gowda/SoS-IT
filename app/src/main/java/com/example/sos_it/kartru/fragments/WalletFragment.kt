package com.example.sos_it.kartru.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sos_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WalletFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_wallet, container, false)
        val amount=view.findViewById<TextView>(R.id.wallet_amount)
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dbref=FirebaseDatabase.getInstance().getReference("Delivery/$uid")
        dbref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

                    var wall=snapshot.child("wallet").value.toString()
                    wall="\u20B9"+wall
                    amount.text= wall
                    amount.visibility=View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        return view
    }
}