package com.skp3214.dailytick

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skp3214.dailytick.database.AppDatabase
import com.skp3214.dailytick.databinding.ActivityMainBinding
import com.skp3214.dailytick.models.Task
import com.skp3214.dailytick.ui.fragments.HomeFragment
import com.skp3214.dailytick.ui.fragments.NotificationsFragment
import com.skp3214.dailytick.ui.fragments.SettingsFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "task-database"
        ).build()

        // Insert dummy data if empty
        lifecycleScope.launch {
            val taskCount = db.taskDao().getAllCount()
            if (taskCount == 0) {
                db.taskDao().insertAll(
                    Task(1, "Task 1", "Description 1", "2025-10-05", "High"),
                    Task(2, "Task 2", "Description 2", "2025-10-06", "Medium"),
                    Task(3, "Task 3", "Description 3", "2025-10-07", "Low")
                )
            }
        }

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