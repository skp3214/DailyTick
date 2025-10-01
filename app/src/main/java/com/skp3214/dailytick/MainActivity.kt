package com.skp3214.dailytick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skp3214.dailytick.databinding.ActivityMainBinding
import com.skp3214.dailytick.ui.fragments.HomeFragment
import com.skp3214.dailytick.ui.fragments.NotificationsFragment
import com.skp3214.dailytick.ui.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val homeFragment = HomeFragment()
        val notificationsFragment = NotificationsFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> setCurrentFragment(homeFragment)
                R.id.nav_notifications -> setCurrentFragment(notificationsFragment)
                R.id.nav_settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, fragment)
            commit()
        }
}