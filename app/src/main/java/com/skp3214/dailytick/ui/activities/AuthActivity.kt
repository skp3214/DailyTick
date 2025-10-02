package com.skp3214.dailytick.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.skp3214.dailytick.databinding.ActivityAuthBinding
import com.skp3214.dailytick.MainActivity
import com.skp3214.dailytick.R
import com.skp3214.dailytick.ui.fragments.OtpFragment
import com.skp3214.dailytick.ui.fragments.SignInFragment
import com.skp3214.dailytick.ui.fragments.SignUpFragment
import com.skp3214.dailytick.ui.viewmodel.AuthViewModel
import com.skp3214.dailytick.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val authViewModel: AuthViewModel by viewModels()

    // Store temporary credentials for OTP verification
    private var tempEmail: String = ""
    private var tempPassword: String = ""
    private var isSignupFlow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeAuthState()

        if (savedInstanceState == null) {
            replaceFragment(SignUpFragment())
        }
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.SignInSuccess -> {
                        onAuthSuccess()
                    }
                    else -> {
                        // Handle other states in fragments
                    }
                }
            }
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

    fun navigateToOtp(isSignup: Boolean, email: String = "", password: String = "") {
        tempEmail = email
        tempPassword = password
        isSignupFlow = isSignup
        val fragment = OtpFragment.newInstance(isSignup)
        replaceFragment(fragment)
    }

    fun onAuthSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun completeAuthentication() {
        if (isSignupFlow) {
            // For signup, the user is already registered in the database
            // Just log them in after OTP verification
            authViewModel.signIn(tempEmail, tempPassword)
        } else {
            // For signin, perform the actual login
            authViewModel.signIn(tempEmail, tempPassword)
        }
    }

    // Remove the separate getAuthViewModel() function to avoid platform declaration clash
    // Fragments can access authViewModel directly or we'll provide it through a different method name
    fun provideAuthViewModel() = authViewModel
}