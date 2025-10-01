package com.skp3214.dailytick.database

import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = kotlinx.coroutines.flow.flow { emit(taskDao.getAll()) }

    suspend fun insertAll(vararg tasks: Task) {
        taskDao.insertAll(*tasks)
    }
}