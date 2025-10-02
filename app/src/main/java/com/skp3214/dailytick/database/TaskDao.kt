package com.skp3214.dailytick.database

import androidx.room.*
import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND userEmail = :userEmail ORDER BY createdAt DESC")
    fun getPendingTasks(userEmail: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 AND userEmail = :userEmail ORDER BY completedAt DESC")
    fun getCompletedTasks(userEmail: String): Flow<List<Task>>

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("UPDATE tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean, completedAt: Long?)
}