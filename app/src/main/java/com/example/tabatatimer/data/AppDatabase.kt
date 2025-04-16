package com.example.tabatatimer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tabatatimer.data.dao.WorkoutDao
import com.example.tabatatimer.data.model.Workout

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun workoutDao(): WorkoutDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tabata_timer_database"
                ).fallbackToDestructiveMigration()
                 .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
} 