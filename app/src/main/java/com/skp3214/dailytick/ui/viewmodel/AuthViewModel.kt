package com.skp3214.dailytick.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skp3214.dailytick.database.TaskDatabase
import com.skp3214.dailytick.database.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskDatabase.getDatabase(application)
    private val userRepository = UserRepository(database.userDao())

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                _currentUser.value = user.email
                _authState.value = AuthState.SignInSuccess
            } else {
                _currentUser.value = null
                _authState.value = AuthState.Idle
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = userRepository.registerUser(email, password)
            if (success) {
                val loginSuccess = userRepository.loginUser(email, password)
                if (loginSuccess) {
                    _currentUser.value = email
                    _authState.value = AuthState.SignInSuccess
                } else {
                    _authState.value = AuthState.Error("Registration successful but login failed")
                }
            } else {
                _authState.value = AuthState.Error("User already exists")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = userRepository.loginUser(email, password)
            if (success) {
                _currentUser.value = email
                _authState.value = AuthState.SignInSuccess
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.logoutCurrentUser()
            _currentUser.value = null
            _authState.value = AuthState.SignedOut
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object SignUpSuccess : AuthState()
    object SignInSuccess : AuthState()
    object SignedOut : AuthState()
    data class Error(val message: String) : AuthState()
}
