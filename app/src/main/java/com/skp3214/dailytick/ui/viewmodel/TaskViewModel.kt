package com.skp3214.dailytick.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.skp3214.dailytick.database.AppDatabase
import com.skp3214.dailytick.database.TaskRepository
import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.Flow

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    val allTasks: Flow<List<Task>>

    init {
        val taskDao = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "task-database"
        ).build().taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }
}