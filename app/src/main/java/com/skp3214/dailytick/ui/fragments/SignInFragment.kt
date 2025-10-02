package com.skp3214.dailytick.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.skp3214.dailytick.databinding.FragmentSignInBinding
import com.skp3214.dailytick.ui.activities.AuthActivity
import com.skp3214.dailytick.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: com.skp3214.dailytick.ui.viewmodel.AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = (activity as AuthActivity).provideAuthViewModel()
        observeAuthState()

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            (activity as AuthActivity).navigateToOtp(false, email, password)
        }

        binding.tvSignUp.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.btnSignIn.isEnabled = false
                        binding.btnSignIn.text = "Signing In..."
                    }
                    is AuthState.SignInSuccess -> {
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignIn.text = "Sign In"
                        authViewModel.resetAuthState()
                    }
                    is AuthState.Error -> {
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignIn.text = "Sign In"
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        authViewModel.resetAuthState()
                    }
                    else -> {
                        binding.btnSignIn.isEnabled = true
                        binding.btnSignIn.text = "Sign In"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}