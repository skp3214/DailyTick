package com.skp3214.dailytick.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.skp3214.dailytick.database.TaskDatabase
import com.skp3214.dailytick.database.TaskRepository
import com.skp3214.dailytick.database.UserRepository
import com.skp3214.dailytick.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskDatabase.getDatabase(application)
    private val taskRepository = TaskRepository(database.taskDao())
    private val userRepository = UserRepository(database.userDao())

    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasks: StateFlow<List<Task>> = _pendingTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    init {
        refreshTasks()
    }

    private fun refreshTasks() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                _currentUser.value = user.email

                taskRepository.getPendingTasks(user.email).collect { tasks ->
                    _pendingTasks.value = tasks
                }
            } else {
                _currentUser.value = null
                _pendingTasks.value = emptyList()
            }
        }

        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                taskRepository.getCompletedTasks(user.email).collect { tasks ->
                    _completedTasks.value = tasks
                }
            } else {
                _completedTasks.value = emptyList()
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                val taskWithUser = task.copy(userEmail = user.email)
                taskRepository.insert(taskWithUser)
                refreshTasksForCurrentUser()
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
            refreshTasksForCurrentUser()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.delete(task)
            refreshTasksForCurrentUser()
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted,
                completedAt = if (!task.isCompleted) Date() else null
            )
            taskRepository.update(updatedTask)
            refreshTasksForCurrentUser()
        }
    }

    private fun refreshTasksForCurrentUser() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                taskRepository.getPendingTasks(user.email).collect { tasks ->
                    _pendingTasks.value = tasks
                }
            }
        }

        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                taskRepository.getCompletedTasks(user.email).collect { tasks ->
                    _completedTasks.value = tasks
                }
            }
        }
    }
}