package com.example.sos_it.karma.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sos_it.R
import com.example.sos_it.karma.adapter.CartAdapter
import com.example.sos_it.karma.model.CartItem
import com.example.sos_it.main.Karma
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.activity_cart.*
import java.util.*
import kotlin.collections.ArrayList


class Cart : AppCompatActivity(){
    private lateinit var dbref: DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private var type=""
    lateinit var userArrayList: ArrayList<CartItem>
    private lateinit var address:String
    private var finalprice=0.0f
    private var fprice=""
    private  val UPI_PAYMENT:Int=0
    private lateinit var latLng: String
    private lateinit var rest:String
    private lateinit var msg: String
    private lateinit var resname:String
    private lateinit var list:ArrayList<CartItem>
    private var tree: Any? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        list= arrayListOf()
        msg = (153723052000..200123021537).random().toString()
        val spinner=findViewById<MaterialSpinner>(R.id.paymenttype)
        spinner.setItems("COD","UPI")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            if(item.toString() != "Pay Using"){
                type=item.toString()
                textpay.text=type
                if(loc.text.isNotEmpty()) {
                    pricetag.isEnabled = true
                    pricetag.setCardBackgroundColor(resources.getColor(R.color.orange))
                }
            }
        }
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val dbref=FirebaseDatabase.getInstance().getReference("Users/$uid/Cart")
        dbref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    list.clear()
                    for(item in snapshot.children){
                        val value=item.getValue(CartItem::class.java)
                        list.add(value!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        rest= intent.getStringExtra("Rest").toString()
        resname=intent.getStringExtra("restname").toString()
        val loc=findViewById<TextView>(R.id.loc)
        loc.setOnClickListener {
            startActivity(Intent(this, Homemaps::class.java).putExtra("Rest",rest).putExtra("restname",resname))
        }
        close_cart.setOnClickListener {
            startActivity(Intent(this, Karma::class.java))
        }
        val addr=intent.getStringExtra("addr")
       latLng=intent.getStringExtra("latLng").toString()
        if(addr!=null){
            FirebaseDatabase.getInstance().getReference("Users/$uid").child("Delivery Address").setValue(addr.toString())
            address=addr.toString()
            loc.text=addr.toString()
        }
        if(loc.text.isEmpty()){
            pricetag.isEnabled=false
            pricetag.setCardBackgroundColor(resources.getColor(R.color.lightorange))
            Toast.makeText(this,"Add delivery address to continue",Toast.LENGTH_SHORT).show()
        }
        if(type==""){
            pricetag.isEnabled=false
            pricetag.setCardBackgroundColor(resources.getColor(R.color.lightorange))
        }
        userRecyclerView=findViewById(R.id.fcartlist)
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList=arrayListOf()
        getCartData()
        pricetag.setOnClickListener {
            if (type == "COD") {
                notification()
                val ref=FirebaseDatabase.getInstance().reference.child("Orders").child(msg)
                ref.child("user").setValue(uid)
                ref.child("destination").setValue(address)
                ref.child("Dest Loc").setValue(latLng)
                ref.child("source").setValue(rest)
                ref.child("Name").setValue(resname)
                ref.child("payment status").setValue("Not Yet Paid")
                ref.child("Order_ID").setValue(msg)
                for (i in list.indices){
                    ref.child("Cart").child((i+1).toString()).setValue(list[i])
                }
                FirebaseDatabase.getInstance().getReference("Users/$uid/Cart").removeValue()
                startActivity(Intent(this, OrderConfirmation::class.java))
            } else {
                val upiId = "8105529823@okbizaxis"
                val name = "SoS-IT"
                payUsingUpi(fprice, upiId, name, msg)
            }
        }


    }
    private fun payUsingUpi(amount: String, upiId: String, name: String, msg: String) {
        val uri=Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa",upiId)
            .appendQueryParameter("pn",name)
            .appendQueryParameter("tn",msg)
            .appendQueryParameter("am",amount)
            .appendQueryParameter("cu","INR")
            .appendQueryParameter("mc","")
            .appendQueryParameter("tr","1537641537")
            .build()

        val upiIntent=Intent(Intent.ACTION_VIEW)
        upiIntent.data = uri

        val chooser=Intent.createChooser(upiIntent,"Pay with")
        if(null!=chooser.resolveActivity(packageManager)){
            startActivityForResult(chooser,UPI_PAYMENT)
        }
        else{
            Toast.makeText(this,"No UPI app found,please install one to continue",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            UPI_PAYMENT->if(RESULT_OK==resultCode || resultCode == 11){
                if(data!=null){
                    val resp=data.getStringExtra("response")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add(resp!!)
                    upiPaymentDataOperation(dataList)
                }
                else {
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            }
            else{
                val dataList: ArrayList<String> = ArrayList()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(dataList: ArrayList<String>) {
        if (isConnectionAvailable(this)) {
            val str = dataList[0]
            var paymentCancel = ""
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&").toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=").toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
                        status = equalStr[1].lowercase(Locale.getDefault())
                    } else if (equalStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
                            Locale.getDefault()
                        ) || equalStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(Locale.getDefault())
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                notification()
                Toast.makeText(this, "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val ref = FirebaseDatabase.getInstance().reference.child("Orders").child(msg)
                ref.child("user").setValue(uid)
                ref.child("destination").setValue(address)
                ref.child("Dest Loc").setValue(latLng)
                ref.child("source").setValue(rest)
                ref.child("Name").setValue(resname)
                ref.child("payment status").setValue("Paid")
                ref.child("Order_ID").setValue(msg)
                ref.child("Cart").setValue(tree)
                startActivity(Intent(this, OrderConfirmation::class.java))
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun notification() {
        val channel= NotificationChannel("n","n",NotificationManager.IMPORTANCE_HIGH)
        val manager:NotificationManager=getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        val sound:Uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder:NotificationCompat.Builder=NotificationCompat.Builder(this,"n")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setContentText("Don't worry! We won't keep you waiting for long.")
            .setContentTitle("Your order is placed")
            .setSound(sound)
        val managerCompat:NotificationManagerCompat= NotificationManagerCompat.from(this)
        managerCompat.notify(999,builder.build())
    }


    private fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected
            && netInfo.isConnectedOrConnecting
            && netInfo.isAvailable
        ) {
            return true
        }
        return false
    }


    private fun getCartData() {
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref= FirebaseDatabase.getInstance().getReference("Users/$uid/Cart")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                finalprice=0.0f
                if(snapshot.exists()){
                    userArrayList.clear()
                    for(userSnapshot in snapshot.children){
                        val user=userSnapshot.getValue(CartItem::class.java)
                        val price= user?.itemprice?.substring(3)?.toFloat()
                        val count=user?.itemcount?.toInt()
                        if(price!=null && count!=null) {
                            finalprice += price.times(count!!)
                        }
                        userArrayList.add(user!!)
                    }
                    if(finalprice==0.0f)
                        pricetag.visibility=View.INVISIBLE
                    val finalpr=findViewById<TextView>(R.id.finalprice)
                    val itemtotal=findViewById<TextView>(R.id.total_item_amount)
                    val taxes=findViewById<TextView>(R.id.taxes)
                    val final="\u20B9"+finalprice.toString()
                    itemtotal.text=final
                    val tax=finalprice*0.05
                    val taxtv="\u20B9"+ String.format("%.2f",tax.toFloat())
                    taxes.text=taxtv
                    var total=(finalprice+tax+30).toString()
                    total="\u20B9"+total
                    finalpr.text= total
                    fprice=finalpr.text.toString()
                    userRecyclerView.adapter= CartAdapter(userArrayList)
                }
                else{
                    userArrayList.clear()
                    textView29.visibility=View.INVISIBLE
                    textView30.visibility=View.INVISIBLE
                    textView31.visibility=View.INVISIBLE
                    total_item_amount.visibility=View.INVISIBLE
                    dcharges.visibility=View.INVISIBLE
                    taxes.visibility=View.INVISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}

