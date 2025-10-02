package com.skp3214.dailytick.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.skp3214.dailytick.databinding.FragmentOtpBinding
import com.skp3214.dailytick.ui.activities.AuthActivity
import com.skp3214.dailytick.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private var countDownTimer: CountDownTimer? = null
    private var isSignup = false

    private lateinit var authViewModel: com.skp3214.dailytick.ui.viewmodel.AuthViewModel

    companion object {
        private const val ARG_IS_SIGNUP = "is_signup"

        fun newInstance(isSignup: Boolean): OtpFragment {
            val fragment = OtpFragment()
            val args = Bundle()
            args.putBoolean(ARG_IS_SIGNUP, isSignup)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSignup = arguments?.getBoolean(ARG_IS_SIGNUP, false) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = (activity as AuthActivity).provideAuthViewModel()
        observeAuthState()

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()
            if (otp.length == 6) {
                (activity as AuthActivity).completeAuthentication()
            } else {
                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResendOtp.setOnClickListener {
            startTimer()
            Toast.makeText(context, "Please enter any 6-digit number to proceed", Toast.LENGTH_SHORT).show()
        }

        startTimer()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.btnVerifyOtp.isEnabled = false
                        binding.btnVerifyOtp.text = "Verifying..."
                    }
                    is AuthState.SignInSuccess -> {
                        binding.btnVerifyOtp.isEnabled = true
                        binding.btnVerifyOtp.text = "Verify OTP"
                        (activity as AuthActivity).onAuthSuccess()
                        authViewModel.resetAuthState()
                    }
                    is AuthState.Error -> {
                        binding.btnVerifyOtp.isEnabled = true
                        binding.btnVerifyOtp.text = "Verify OTP"
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        authViewModel.resetAuthState()
                    }
                    else -> {
                        binding.btnVerifyOtp.isEnabled = true
                        binding.btnVerifyOtp.text = "Verify OTP"
                    }
                }
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        binding.btnResendOtp.isEnabled = false
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = "${millisUntilFinished / 1000} seconds"
            }

            override fun onFinish() {
                binding.tvTimer.text = "Time's up"
                binding.btnResendOtp.isEnabled = true
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}