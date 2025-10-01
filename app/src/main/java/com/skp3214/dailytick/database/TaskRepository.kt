package com.skp3214.dailytick.database

import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getPendingTasks(): Flow<List<Task>> = flow {
        emit(taskDao.getPendingTasks())
    }

    fun getCompletedTasks(): Flow<List<Task>> = flow {
        emit(taskDao.getCompletedTasks())
    }

    suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }
}