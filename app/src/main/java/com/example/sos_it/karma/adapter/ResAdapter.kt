package com.example.sos_it.karma.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.activities.Menu
import com.example.sos_it.karma.model.Rest
import com.squareup.picasso.Picasso

class ResAdapter(private val userList: ArrayList<Rest>):RecyclerView.Adapter<ResAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.res_item,parent,false)
        val resitem=itemView.findViewById<CardView>(R.id.resitem)
        val resname=itemView.findViewById<TextView>(R.id.tvname)
        val restaddr=itemView.findViewById<TextView>(R.id.rest)
        resitem.setOnClickListener {
            val intent=Intent(parent.context, Menu::class.java)
            intent.putExtra("Path","Menu/"+resname.text.toString())
            intent.putExtra("restaddr",restaddr.text.toString())
            intent.putExtra("restname",resname.text.toString())
            parent.context.startActivity(intent)
        }
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     val currentitem=userList[position]
        Picasso.get().load(currentitem.image).into(holder.image)
        holder.name.text=currentitem.name
        holder.tag.text=currentitem.tag
        holder.loc.text=currentitem.loc
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val image:ImageView=itemView.findViewById(R.id.ivimage)
        val name:TextView=itemView.findViewById(R.id.tvname)
        val tag:TextView=itemView.findViewById(R.id.tvtag)
        val loc:TextView=itemView.findViewById(R.id.rest)


    }
}