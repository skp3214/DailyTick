package com.skp3214.dailytick.database

import androidx.room.*
import com.skp3214.dailytick.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    suspend fun getAll(): List<Task>

    @Query("SELECT COUNT(*) FROM task")
    suspend fun getAllCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tasks: Task)

    @Delete
    suspend fun delete(task: Task)
}