package com.example.tabatatimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.data.AppDatabase
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.data.repository.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: WorkoutRepository
    val allWorkouts: LiveData<List<Workout>>
    
    init {
        val workoutDao = AppDatabase.getDatabase(application).workoutDao()
        repository = WorkoutRepository(workoutDao)
        allWorkouts = repository.allWorkouts
    }
    
    fun getWorkoutById(id: Long): LiveData<Workout> {
        return repository.getWorkoutById(id)
    }
    
    fun insert(workout: Workout, callback: (Long) -> Unit = {}) = viewModelScope.launch(Dispatchers.IO) {
        val id = repository.insert(workout)
        launch(Dispatchers.Main) { callback(id) }
    }
    
    fun update(workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(workout)
    }
    
    fun delete(workout: Workout) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(workout)
    }
    
    fun deleteAllWorkouts() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllWorkouts()
    }
} 