package com.example.sos_it.karma.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.karma.activities.Menu
import com.example.sos_it.R
import com.example.sos_it.karma.model.SearchItem
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
class SearchAdapter(private val userList: ArrayList<SearchItem>):RecyclerView.Adapter<SearchAdapter.MySearchViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySearchViewHolder {
       val itemView=LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        val item:CardView=itemView.findViewById(R.id.search_item)
        val name:TextView=itemView.findViewById(R.id.search_name)
        val addr:TextView=itemView.findViewById(R.id.textView27)
        item.setOnClickListener {
            val intent= Intent(parent.context, Menu::class.java)
            intent.putExtra("Path","Menu/"+name.text.toString())
            intent.putExtra("restaddr",addr.text.toString())
            parent.context.startActivity(intent)
        }
        return MySearchViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MySearchViewHolder, position: Int) {
        val currentitem=userList[position]
        holder.name.text=currentitem.name
        holder.addr.text=currentitem.loc
        Picasso.get().load(currentitem.image).into(holder.image)
    }
    override fun getItemCount(): Int {
       return userList.size
    }
    class MySearchViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val image:CircleImageView=itemView.findViewById(R.id.search_image)
    val name:TextView=itemView.findViewById(R.id.search_name)
    val addr:TextView=itemView.findViewById(R.id.textView27)
    }
}