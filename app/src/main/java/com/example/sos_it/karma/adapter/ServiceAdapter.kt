package com.example.sos_it.karma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.Service

class ServiceAdapter(private val servicelist:ArrayList<Service>):RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name:TextView=itemView.findViewById(R.id.sername)
        val addr:TextView=itemView.findViewById(R.id.seraddr)
        val phone:TextView=itemView.findViewById(R.id.serphone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.service_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=servicelist[position]
        holder.name.text=currentitem.name.toString()
        holder.addr.text=currentitem.address.toString()
        holder.phone.text=currentitem.phone.toString()
    }

    override fun getItemCount(): Int {
        return servicelist.size
    }
}