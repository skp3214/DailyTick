package com.skp3214.dailytick.database

import androidx.room.*
import com.skp3214.dailytick.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE isCompleted = 0 ORDER BY dueDate ASC")
    suspend fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM task WHERE isCompleted = 1 ORDER BY dueDate ASC")
    suspend fun getCompletedTasks(): List<Task>


    @Query("SELECT COUNT(*) FROM task")
    suspend fun getAllCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)
}