package com.example.tabatatimer.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.data.repository.WorkoutRepository

/**
 * Utility class to pre-populate the database with sample workouts on first run.
 */
class DataInitializer(private val context: Context, private val repository: WorkoutRepository) {
    
    companion object {
        private const val PREFS_NAME = "com.example.tabatatimer.prefs"
        private const val KEY_DATA_INITIALIZED = "data_initialized"
    }
    
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Check if the database needs to be pre-populated with sample data.
     * If it does, add some sample workouts.
     */
    suspend fun initializeSampleDataIfNeeded() {
        val initialized = prefs.getBoolean(KEY_DATA_INITIALIZED, false)
        
        if (!initialized) {
            val workouts = createSampleWorkouts()
            
            for (workout in workouts) {
                repository.insert(workout)
            }
            
            prefs.edit {
                putBoolean(KEY_DATA_INITIALIZED, true)
            }
        }
    }
    
    /**
     * Create a list of sample workouts.
     */
    private fun createSampleWorkouts(): List<Workout> {
        return listOf(
            Workout(
                name = "Classic Tabata",
                description = "The original Tabata protocol: 20 seconds work, 10 seconds rest for 8 cycles.",
                warmupDuration = 10,
                workDuration = 20,
                restDuration = 10,
                cycles = 8,
                sets = 1,
                restBetweenSets = 60,
                cooldownDuration = 10
            ),
            Workout(
                name = "Beginner Tabata",
                description = "A more manageable version for beginners with longer rest periods.",
                warmupDuration = 15,
                workDuration = 20,
                restDuration = 20,
                cycles = 6,
                sets = 1,
                restBetweenSets = 60,
                cooldownDuration = 15
            ),
            Workout(
                name = "Advanced Tabata",
                description = "A challenging workout with 2 sets of 8 cycles for a total of 16 intervals.",
                warmupDuration = 10,
                workDuration = 20,
                restDuration = 10,
                cycles = 8,
                sets = 2,
                restBetweenSets = 60,
                cooldownDuration = 10
            ),
            Workout(
                name = "Extended Work",
                description = "Longer work intervals with proportionally longer rests.",
                warmupDuration = 15,
                workDuration = 40,
                restDuration = 20,
                cycles = 6,
                sets = 1,
                restBetweenSets = 60,
                cooldownDuration = 15
            )
        )
    }
} 