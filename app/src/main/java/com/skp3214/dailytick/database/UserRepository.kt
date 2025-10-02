package com.skp3214.dailytick.database

import com.skp3214.dailytick.models.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()
    }

    suspend fun registerUser(email: String, password: String): Boolean {
        val existingUser = userDao.getUserByEmail(email)
        return if (existingUser == null) {
            val newUser = User(email = email, password = password, isLoggedIn = false)
            userDao.insertUser(newUser)
            true
        } else {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            userDao.logoutAllUsers()
            userDao.loginUser(email)
            true
        } else {
            false
        }
    }

    suspend fun logoutCurrentUser() {
        val currentUser = userDao.getCurrentUser()
        currentUser?.let {
            userDao.logoutUser(it.email)
        }
    }
}
