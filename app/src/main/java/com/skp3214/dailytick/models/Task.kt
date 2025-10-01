package com.skp3214.dailytick.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val priority: String
)