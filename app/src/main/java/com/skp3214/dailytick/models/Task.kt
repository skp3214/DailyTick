package com.skp3214.dailytick.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skp3214.dailytick.data.converters.DateConverter
import java.io.Serializable
import java.util.Date

@Entity(tableName = "tasks")
@TypeConverters(DateConverter::class)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val priority: String = "LOW",
    val dueDate: Date? = null,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val completedAt: Date? = null,
    val userEmail: String
) : Serializable
