package com.skp3214.dailytick

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skp3214.dailytick.databinding.ActivityMainBinding
import com.skp3214.dailytick.ui.activities.AuthActivity
import com.skp3214.dailytick.ui.fragments.HomeFragment
import com.skp3214.dailytick.ui.fragments.NotificationsFragment
import com.skp3214.dailytick.ui.fragments.SettingsFragment
import com.skp3214.dailytick.ui.viewmodel.AuthState
import com.skp3214.dailytick.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthenticationAndSetupUI()
    }

    private fun checkAuthenticationAndSetupUI() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.SignInSuccess -> {
                        setupMainUI()
                    }
                    is AuthState.Idle, is AuthState.SignedOut -> {
                        redirectToAuth()
                    }
                    is AuthState.Loading -> {
                    }
                    else -> {
                        redirectToAuth()
                    }
                }
            }
        }
    }

    private fun setupMainUI() {
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

    private fun redirectToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_container, fragment)
            commit()
        }
}