package com.example.tabatatimer.data.repository

import androidx.lifecycle.LiveData
import com.example.tabatatimer.data.dao.WorkoutDao
import com.example.tabatatimer.data.model.Workout

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    
    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAllWorkouts()
    
    fun getWorkoutById(id: Long): LiveData<Workout> {
        return workoutDao.getWorkoutById(id)
    }
    
    suspend fun insert(workout: Workout): Long {
        return workoutDao.insertWorkout(workout)
    }
    
    suspend fun update(workout: Workout) {
        workout.updatedAt = System.currentTimeMillis()
        workoutDao.updateWorkout(workout)
    }
    
    suspend fun delete(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }
    
    suspend fun deleteAllWorkouts() {
        workoutDao.deleteAllWorkouts()
    }
} 