package com.skp3214.dailytick.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.skp3214.dailytick.database.AppDatabase
import com.skp3214.dailytick.database.TaskRepository
import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "task-database"
    ).fallbackToDestructiveMigration()
     .build()

    private val repository = TaskRepository(database.taskDao())

    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasks: StateFlow<List<Task>> = _pendingTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks.asStateFlow()

    init {
        refreshTasks()
    }

    private fun refreshTasks() {
        viewModelScope.launch {
            repository.getPendingTasks().collect { tasks ->
                _pendingTasks.value = tasks
            }
        }

        viewModelScope.launch {
            repository.getCompletedTasks().collect { tasks ->
                _completedTasks.value = tasks
            }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insert(task)
            refreshTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task)
            refreshTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
            refreshTasks()
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.updateTaskCompletion(task.id, !task.isCompleted)
            refreshTasks()
        }
    }
}