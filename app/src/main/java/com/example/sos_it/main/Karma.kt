package com.example.sos_it.main


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.sos_it.R
import com.example.sos_it.karma.fragments.CartFragment
import com.example.sos_it.karma.fragments.HomeFragment
import com.example.sos_it.karma.fragments.SearchFragment
import com.example.sos_it.karma.fragments.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Karma : AppCompatActivity() {
    private var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karma)
        val bottomnav = findViewById<MeowBottomNavigation>(R.id.mbnav)
        bottomnav.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottomnav.add(MeowBottomNavigation.Model(2, R.drawable.ic_search))
        bottomnav.add(MeowBottomNavigation.Model(3, R.drawable.ic__cart))
        bottomnav.add(MeowBottomNavigation.Model(4, R.drawable.ic_settings))
        bottomnav.setOnShowListener { item ->
            var fragment: Fragment? = null
            when (item.id) {
                1 -> fragment = HomeFragment()
                2 -> fragment = SearchFragment()
                3 -> fragment = CartFragment()
                4->fragment= SettingsFragment()
            }
            loadFragment(fragment)
        }
        val uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val ref=FirebaseDatabase.getInstance().getReference("Users/$uid/Cart")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count= snapshot.childrenCount.toInt()
                if(count>0)
                    bottomnav.setCount(3,count.toString())
                else
                    bottomnav.clearCount(3)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        bottomnav.show(1,true)
        bottomnav.setOnClickMenuListener(MeowBottomNavigation.ClickListener {

        })
    }

    override fun onBackPressed() {
       startActivity(Intent(this, HomePage::class.java))
    }

    private fun loadFragment(fragment: Fragment?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.Frame_layout, fragment!!)
            .commit()
    }
}