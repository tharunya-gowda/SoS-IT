package com.example.sos_it.karma.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class CartAdapter(private val cartlist: ArrayList<CartItem>):RecyclerView.Adapter<CartAdapter.MyCartViewHolder>() {
    private var totalp=0.0f
    private var count by Delegates.notNull<Int>()
    private var price by Delegates.notNull<Float>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.cart_item,parent,false)
        val additem=itemView.findViewById<ImageButton>(R.id.add_cart_item)
        val name=itemView.findViewById<TextView>(R.id.itemname)
        val removeitem=itemView.findViewById<ImageButton>(R.id.remove_cart_item)
        val item=itemView.findViewById<CardView>(R.id.cartitem)
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref=FirebaseDatabase.getInstance().getReference("Users/$uid").child("Cart")
        additem.setOnClickListener {
            count+=1
            val db=ref.child(name.text.toString())
            db.child("itemcount").setValue(count.toString())

        }
        removeitem.setOnClickListener {
            count-=1
            val db=ref.child(name.text.toString())
            db.child("itemcount").setValue(count.toString())
            if(count==0){
                db.removeValue()
                item.visibility=View.GONE
            }

        }

        return MyCartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        val currentitem=cartlist[position]
        holder.itemname.text=currentitem.itemname.toString()
        holder.itemprice.text=currentitem.itemprice.toString()
        holder.itemcount.text=currentitem.itemcount.toString()
        count=currentitem.itemcount!!.toInt()
        price=currentitem.itemprice?.substring(3)?.toFloat()!!
        totalp=currentitem.itemprice?.substring(3)?.toFloat()!!*currentitem.itemcount!!.toInt()
        holder.itemtotal.text=totalp.toString()
    }

    override fun getItemCount(): Int {
        return cartlist.size
    }
    class MyCartViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val itemname=itemView.findViewById<TextView>(R.id.itemname)
        val itemprice=itemView.findViewById<TextView>(R.id.itemprice)
        val itemcount=itemView.findViewById<TextView>(R.id.Tv_cart_itemcount)
        val itemtotal=itemView.findViewById<TextView>(R.id.itemtotal)
    }

}