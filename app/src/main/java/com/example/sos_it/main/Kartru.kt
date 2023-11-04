package com.example.sos_it.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.sos_it.R
import com.example.sos_it.kartru.fragments.MenuFragment
import com.example.sos_it.kartru.fragments.WalletFragment
import com.example.sos_it.kartru.fragments.WorkFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class Kartru : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kartru)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        val bottomnav=findViewById<MeowBottomNavigation>(R.id.mbnav_kartru)
        bottomnav.add(MeowBottomNavigation.Model(1, R.drawable.ic_work))
        bottomnav.add(MeowBottomNavigation.Model(2, R.drawable.ic_wallet))
        bottomnav.add(MeowBottomNavigation.Model(3, R.drawable.ic_menu))
bottomnav.setOnShowListener { item ->
            var fragment: Fragment? = null
            when (item.id) {
                1 -> fragment = WorkFragment()
                2 -> fragment = WalletFragment()
                3->fragment= MenuFragment()
            }
            loadFragment(fragment)
        }
        bottomnav.show(1,true)
        bottomnav.setOnClickMenuListener(MeowBottomNavigation.ClickListener {

        })

    }

    private fun loadFragment(fragment: Fragment?) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_kartru, fragment!!)
            .commit()
    }


}