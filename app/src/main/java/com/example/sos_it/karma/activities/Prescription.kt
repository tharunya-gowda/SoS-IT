package com.example.sos_it.karma.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.example.sos_it.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_prescription.*
import java.io.ByteArrayOutputStream

class Prescription : AppCompatActivity() {
    private lateinit var storage:StorageReference
    private var bytes:ByteArrayOutputStream=ByteArrayOutputStream()
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription)
        upload.isEnabled=false
        storage= FirebaseStorage.getInstance().reference.child("Prescriptions")
        addpresc.isEnabled=false
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),111)
        }
        else
            addpresc.isEnabled=true
        addpresc.setOnClickListener(View.OnClickListener {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,101)
        })
        upload.setOnClickListener{
            startActivity(Intent(this, PresAdded::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            val pic:Bitmap?=data?.getParcelableExtra<Bitmap>("data")
            addpresc.setImageBitmap(pic)
            pic?.compress(Bitmap.CompressFormat.JPEG,90,bytes)
            val bb=bytes.toByteArray()

            uploadtoFirebase(bb)

        }
    }

    private fun uploadtoFirebase(bb: ByteArray) {
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val store=storage.child("$uid.jpg")
        val uploadTask=store.putBytes(bb)
        uploadTask.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
            store.downloadUrl.addOnSuccessListener(OnSuccessListener{
                val url=it.toString()
                val imageref=FirebaseDatabase.getInstance().getReference("Users/$uid")
                imageref.child("prescription").setValue(url)
                upload.isEnabled=true
            })
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            addpresc.isEnabled=true
        }
    }
}