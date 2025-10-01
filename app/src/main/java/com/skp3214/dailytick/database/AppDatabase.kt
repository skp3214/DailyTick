package com.skp3214.dailytick.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skp3214.dailytick.models.Task

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}