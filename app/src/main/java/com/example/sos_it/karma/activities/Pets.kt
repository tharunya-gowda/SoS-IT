package com.example.sos_it.karma.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.sos_it.R
import kotlinx.android.synthetic.main.activity_pets.*

class Pets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
        puppy_food.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Puppy Dog Food")
            startActivity(intent)
        }
        adult_food.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Adult Dog Food")
            startActivity(intent)
        }
        adultc_food.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Adult Cat Food")
            startActivity(intent)
        }
        toys.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Accessories and Toys")
            startActivity(intent)
        }
        cat_treats.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Cat Treats")
            startActivity(intent)
        }
        grooming.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Grooming and Hygiene")
            startActivity(intent)
        }
        kitten_food.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Kitten food")
            startActivity(intent)
        }
        medicines.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Medicine and Supplements")
            startActivity(intent)
        }
        dog_treats.setOnClickListener {
            val intent=Intent(this, Medicines::class.java)
            intent.putExtra("Path","Pets/Dog Treats and Chews")
            startActivity(intent)
        }

    }
}