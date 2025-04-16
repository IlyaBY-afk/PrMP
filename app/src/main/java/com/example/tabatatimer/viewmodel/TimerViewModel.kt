package com.example.tabatatimer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.timer.TabataTimer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    
    private var timer: TabataTimer? = null
    
    private val _timerState = MutableLiveData<TabataTimer.TimerState>()
    val timerState: LiveData<TabataTimer.TimerState> = _timerState
    
    private val _currentPhase = MutableLiveData<TabataTimer.Phase>()
    val currentPhase: LiveData<TabataTimer.Phase> = _currentPhase
    
    private val _timeRemaining = MutableLiveData<Int>()
    val timeRemaining: LiveData<Int> = _timeRemaining
    
    private val _progress = MutableLiveData<Float>()
    val progress: LiveData<Float> = _progress
    
    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning
    
    fun setWorkout(workout: Workout) {
        timer?.pause()
        
        timer = TabataTimer(workout).also { newTimer ->
            viewModelScope.launch {
                newTimer.timerState.collectLatest { state ->
                    _timerState.value = state
                    _currentPhase.value = state.phase
                    _timeRemaining.value = state.timeRemaining
                    _progress.value = state.progress
                    _isRunning.value = state.isRunning
                }
            }
        }
    }
    
    fun startTimer() {
        timer?.start()
    }
    
    fun pauseTimer() {
        timer?.pause()
    }
    
    fun resetTimer() {
        timer?.reset()
    }
    
    fun toggleTimer() {
        if (_isRunning.value == true) {
            pauseTimer()
        } else {
            startTimer()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timer?.pause()
    }
    
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
} 