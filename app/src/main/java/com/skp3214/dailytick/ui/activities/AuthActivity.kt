package com.skp3214.dailytick.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.skp3214.dailytick.databinding.ActivityAuthBinding
import androidx.core.content.edit
import com.skp3214.dailytick.MainActivity
import com.skp3214.dailytick.R
import com.skp3214.dailytick.ui.fragments.OtpFragment
import com.skp3214.dailytick.ui.fragments.SignInFragment
import com.skp3214.dailytick.ui.fragments.SignUpFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(SignUpFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_auth_container, fragment)
            .commit()
    }

    fun navigateToSignIn() {
        replaceFragment(SignInFragment())
    }

    fun navigateToOtp(isSignup: Boolean) {
        val fragment = OtpFragment.newInstance(isSignup)
        replaceFragment(fragment)
    }

    fun onAuthSuccess() {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit {
            putBoolean("is_logged_in", true)
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}