package com.example.sos_it.kartru.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.kartru.model.WorkItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WorkAdapter(private val workItemList:ArrayList<WorkItem>):RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.work_item,parent,false)
        val source=itemView.findViewById<TextView>(R.id.source)
        val destination=itemView.findViewById<TextView>(R.id.dest)
        val direction=itemView.findViewById<ImageView>(R.id.direction)
        val orderid=itemView.findViewById<TextView>(R.id.orderid)
        direction.setOnClickListener {
            val addr=source.text.toString().replace(" ","+",true)
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=${addr}"))
            parent.context.startActivity(intent)
        }
        val switch=itemView.findViewById<SwitchCompat>(R.id.switch3)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
                FirebaseDatabase.getInstance().getReference("Delivery/$uid/order").setValue(orderid.text.toString())
                FirebaseDatabase.getInstance().getReference("Orders/${orderid.text}/acceptedby").setValue(uid)
            }
        }
        return WorkViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val currentItem=workItemList[position]
        holder.name.text=currentItem.Name
        holder.orderid.text=currentItem.Order_ID
        holder.destination.text=currentItem.destination
        holder.source.text=currentItem.source
        val uid=currentItem.user.toString()
        val dbref=FirebaseDatabase.getInstance().getReference("Users/$uid")
        dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    holder.username.text=snapshot.child("username").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun getItemCount(): Int {
        return workItemList.size
    }
    class WorkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView =itemView.findViewById(R.id.workname)
        val orderid:TextView=itemView.findViewById(R.id.orderid)
        val destination:TextView=itemView.findViewById(R.id.dest)
        val source:TextView=itemView.findViewById(R.id.source)
        val username:TextView=itemView.findViewById(R.id.workusername)
    }
}