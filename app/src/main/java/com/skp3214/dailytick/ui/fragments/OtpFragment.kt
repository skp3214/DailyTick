package com.skp3214.dailytick.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.skp3214.dailytick.databinding.FragmentOtpBinding
import com.skp3214.dailytick.ui.activities.AuthActivity

class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private var countDownTimer: CountDownTimer? = null
    private var isSignup = false

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

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()
            if (otp.length == 6) {
                // Simulate OTP verification success
                (activity as AuthActivity).onAuthSuccess()
            } else {
                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResendOtp.setOnClickListener {
            startTimer()
            Toast.makeText(context, "OTP resent", Toast.LENGTH_SHORT).show()
        }

        startTimer()
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