package com.example.sos_it.karma.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.Med
import com.example.sos_it.karma.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
class MedAdapter(private val userList: ArrayList<Med>): RecyclerView.Adapter<MedAdapter.MyMedViewHolder>() {
    lateinit var cartlist:ArrayList<CartItem>
    private var total=0
    private var totalprice=0.0f
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMedViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.med_item,parent,false)
        var itemcountval=0
        val itemcount=itemView.findViewById<TextView>(R.id.Tv_itemcount)
        itemcount.text=itemcountval.toString()
        val price=itemView.findViewById<TextView>(R.id.tvprice)
        val additem=itemView.findViewById<ImageButton>(R.id.additem)
        val name=itemView.findViewById<TextView>(R.id.tvmname)
        cartlist= arrayListOf()
        cartlist.clear()
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref=FirebaseDatabase.getInstance().getReference("Users/$uid").child("Cart")
        additem.setOnClickListener(View.OnClickListener {
            itemcountval+=1
            total+=1
            if(itemcountval>0){
                val add=itemView.findViewById<CardView>(R.id.add)
                add.visibility=View.INVISIBLE
                val db=ref.child(name.text.toString())
                db.child("itemprice").setValue(price.text.toString())
                db.child("itemname").setValue(name.text.toString())
                db.child("itemcount").setValue(itemcountval.toString())
            }
            var trimmed=price.text.toString()
            trimmed=trimmed.substring(3)
            totalprice+=trimmed.toFloat()
            val intent=Intent("Cart")
            intent.putExtra("Item",total.toString())
            intent.putExtra("Price",totalprice.toString())
            intent.putExtra("Array",cartlist)
            LocalBroadcastManager.getInstance(parent.context).sendBroadcast(intent)
            itemcount.text=itemcountval.toString()
            val item= CartItem(name.text.toString(),trimmed,itemcountval.toString())
            cartlist.add(item)
        })
        val removeitem=itemView.findViewById<ImageButton>(R.id.removeitem)
        removeitem.setOnClickListener(View.OnClickListener {
            var trimmed=price.text.toString()
            trimmed=trimmed.substring(3)
            totalprice-=trimmed.toFloat()
            if(itemcountval>0) {
                itemcountval -= 1
                total-=1
                val intent=Intent("Cart")
                intent.putExtra("Item",total.toString())
                intent.putExtra("Price",totalprice.toString())
                intent.putExtra("Array",cartlist)
                LocalBroadcastManager.getInstance(parent.context).sendBroadcast(intent)
                itemcount.text=itemcountval.toString()
                cartlist.remove(CartItem(name.text.toString(),trimmed,itemcountval.toString()))
                val db=ref.child(name.text.toString())
                db.child("itemcount").setValue(itemcountval.toString())

            }
            if(itemcountval==0){
                val add=itemView.findViewById<CardView>(R.id.add)
                add.visibility=View.VISIBLE
                ref.child(name.text.toString()).removeValue()
            }
            if(total==0)
                totalprice=0.0f
            itemcount.text=itemcountval.toString()

        })
        return MyMedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyMedViewHolder, position: Int) {
        val currentitem=userList[position]
        Picasso.get().load(currentitem.image).into(holder.image)
        holder.name.text=currentitem.name
        holder.price.text=currentitem.price
        Picasso.get().load(currentitem.type).into(holder.type)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyMedViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val image: ImageView =itemView.findViewById(R.id.ivmed)
        val name: TextView =itemView.findViewById(R.id.tvmname)
        val price: TextView =itemView.findViewById(R.id.tvprice)
        val type:ImageView=itemView.findViewById(R.id.type)

    }
}