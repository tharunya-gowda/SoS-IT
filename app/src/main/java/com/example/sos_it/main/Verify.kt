package com.example.sos_it.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sos_it.R
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase

class Verify : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = this.resources.getColor(R.color.black)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)
        auth=FirebaseAuth.getInstance()
        val storedVerificationId=intent.getStringExtra("storedVerificationId")
        val verify=findViewById<Button>(R.id.btn_otp)
        val otpGiven=findViewById<EditText>(R.id.otp)




        verify.setOnClickListener{
            val otp=otpGiven.text.toString().trim()
            if(otp.isNotEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val name=intent.getStringExtra("name")
                    val number= FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
                    val dbref= FirebaseDatabase.getInstance().getReference("Users")
                    dbref.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .child("User_Phone").setValue(number)
                    dbref.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .child("username").setValue(name)
                    val intent=Intent(applicationContext, HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

}