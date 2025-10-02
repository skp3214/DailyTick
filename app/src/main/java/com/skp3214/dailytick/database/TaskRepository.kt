package com.skp3214.dailytick.database

import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskRepository(private val taskDao: TaskDao) {

    fun getPendingTasks(userEmail: String): Flow<List<Task>> = taskDao.getPendingTasks(userEmail)

    fun getCompletedTasks(userEmail: String): Flow<List<Task>> = taskDao.getCompletedTasks(userEmail)

    suspend fun insert(task: Task): Long {
        return taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTaskCompletion(taskId: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) Date().time else null
        taskDao.updateTaskCompletion(taskId, isCompleted, completedAt)
    }
}