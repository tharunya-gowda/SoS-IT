package com.example.sos_it.karma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.HomeItem
import com.squareup.picasso.Picasso

class HomeAdapter(private val homelist: ArrayList<HomeItem>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.homeitem, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentitem = homelist[position]
        holder.name.text = currentitem.name.toString()
        Picasso.get().load(currentitem.image).into(holder.pic)
    }

    override fun getItemCount(): Int {
        return homelist.size
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.homename)
        val pic: ImageView = itemView.findViewById(R.id.homeimg)
    }

}