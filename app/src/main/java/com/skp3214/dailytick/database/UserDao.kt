package com.skp3214.dailytick.database

import androidx.room.*
import com.skp3214.dailytick.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUser(): User?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getCurrentUserFlow(): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET isLoggedIn = 1 WHERE email = :email")
    suspend fun loginUser(email: String)

    @Query("UPDATE users SET isLoggedIn = 0 WHERE email = :email")
    suspend fun logoutUser(email: String)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()
}
