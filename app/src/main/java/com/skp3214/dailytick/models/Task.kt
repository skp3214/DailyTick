package com.skp3214.dailytick.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String,
    val isCompleted: Boolean = false
) : Serializable
