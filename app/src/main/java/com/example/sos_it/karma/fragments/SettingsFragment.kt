package com.example.sos_it.karma.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.sos_it.main.Login
import com.example.sos_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
            val phone = view.findViewById<TextView>(R.id.phonenumber)
            val name=view.findViewById<TextView>(R.id.name)
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref=FirebaseDatabase.getInstance().getReference("Users/$uid")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()){
                      name.visibility=View.VISIBLE
                      name.text= snapshot.child("username").value.toString()
              }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        FirebaseAuth.getInstance().currentUser?.reload()
            val authen = FirebaseAuth.getInstance()
            val currentuser = authen.currentUser
            if (currentuser != null) {
                phone.text = currentuser.phoneNumber
            }
            val logout = view.findViewById<ImageButton>(R.id.imageButton)
            logout.setOnClickListener {
                AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_error)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Log Out", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(context, Login::class.java)
                            startActivity(intent)
                        }
                    })
                    .setNegativeButton("No", null).show()
            }
            return view
    }
}