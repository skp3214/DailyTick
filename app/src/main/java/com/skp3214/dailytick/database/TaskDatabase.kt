package com.skp3214.dailytick.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.skp3214.dailytick.models.Task
import com.skp3214.dailytick.models.User
import com.skp3214.dailytick.data.converters.DateConverter

@Database(
    entities = [Task::class, User::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "daily_tick_database_v8"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
