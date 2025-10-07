package com.skp3214.dailytick.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.skp3214.dailytick.databinding.FragmentSignUpBinding
import com.skp3214.dailytick.ui.activities.AuthActivity
import com.skp3214.dailytick.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: com.skp3214.dailytick.ui.viewmodel.AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = (activity as AuthActivity).provideAuthViewModel()
        observeAuthState()

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateInputs(email, password, confirmPassword)) {
                (activity as AuthActivity).navigateToOtp(true, email, password)
            }
        }

        binding.tvSignIn.setOnClickListener {
            (activity as AuthActivity).navigateToSignIn()
        }
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.btnSignUp.isEnabled = false
                        binding.btnSignUp.text = "Signing Up..."
                    }
                    is AuthState.SignUpSuccess -> {
                        binding.btnSignUp.isEnabled = true
                        binding.btnSignUp.text = "Sign Up"
                    }
                    is AuthState.Error -> {
                        binding.btnSignUp.isEnabled = true
                        binding.btnSignUp.text = "Sign Up"
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        authViewModel.resetAuthState()
                    }
                    else -> {
                        binding.btnSignUp.isEnabled = true
                        binding.btnSignUp.text = "Sign Up"
                    }
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}