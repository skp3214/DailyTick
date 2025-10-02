package com.skp3214.dailytick.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.skp3214.dailytick.MainActivity
import com.skp3214.dailytick.R
import com.skp3214.dailytick.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLoginStatus()
        }, 2000)
    }

    private fun checkUserLoginStatus() {
        lifecycleScope.launch {
            if (authViewModel.isUserLoggedIn()) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            }
            finish()
        }
    }
}