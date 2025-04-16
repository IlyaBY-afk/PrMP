package com.example.tabatatimer.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabatatimer.data.model.Workout

@Dao
interface WorkoutDao {
    
    @Query("SELECT * FROM workouts ORDER BY updatedAt DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>
    
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutById(workoutId: Long): LiveData<Workout>
    
    @Insert
    suspend fun insertWorkout(workout: Workout): Long
    
    @Update
    suspend fun updateWorkout(workout: Workout)
    
    @Delete
    suspend fun deleteWorkout(workout: Workout)
    
    @Query("DELETE FROM workouts")
    suspend fun deleteAllWorkouts()
} 