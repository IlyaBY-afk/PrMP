package com.example.tabatatimer.timer

import android.os.CountDownTimer
import com.example.tabatatimer.data.model.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TabataTimer(private val workout: Workout) {

    enum class Phase {
        WARMUP, WORK, REST, REST_BETWEEN_SETS, COOLDOWN, FINISHED
    }

    data class TimerState(
        val phase: Phase = Phase.WARMUP,
        val timeRemaining: Int = 0,
        val totalTime: Int = 0,
        val currentCycle: Int = 1,
        val totalCycles: Int = 1,
        val currentSet: Int = 1,
        val totalSets: Int = 1,
        val isRunning: Boolean = false,
        val progress: Float = 0f
    )

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState

    private var countDownTimer: CountDownTimer? = null
    
    private var totalTimeMillis: Long = 0
    private var phaseTimeMillis: Long = 0

    init {
        reset()
    }

    fun start() {
        if (_timerState.value.isRunning) return
        
        startTimer()
        _timerState.value = _timerState.value.copy(isRunning = true)
    }

    fun pause() {
        countDownTimer?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false)
    }

    fun reset() {
        countDownTimer?.cancel()
        
        val warmupDuration = workout.warmupDuration
        totalTimeMillis = calculateTotalTime(workout) * 1000L
        
        _timerState.value = TimerState(
            phase = Phase.WARMUP,
            timeRemaining = warmupDuration,
            totalTime = calculateTotalTime(workout),
            currentCycle = 1,
            totalCycles = workout.cycles,
            currentSet = 1, 
            totalSets = workout.sets,
            isRunning = false,
            progress = 0f
        )
        
        phaseTimeMillis = warmupDuration * 1000L
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        
        countDownTimer = object : CountDownTimer((_timerState.value.timeRemaining * 1000).toLong(), 100) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                val progress = 1f - (millisUntilFinished.toFloat() / phaseTimeMillis.toFloat())
                
                _timerState.value = _timerState.value.copy(
                    timeRemaining = secondsRemaining,
                    progress = progress
                )
            }

            override fun onFinish() {
                moveToNextPhase()
            }
        }.start()
    }

    private fun moveToNextPhase() {
        val currentState = _timerState.value
        var newPhase = currentState.phase
        var newCycle = currentState.currentCycle
        var newSet = currentState.currentSet
        var nextDuration = 0

        when (currentState.phase) {
            Phase.WARMUP -> {
                newPhase = Phase.WORK
                nextDuration = workout.workDuration
            }
            Phase.WORK -> {
                if (currentState.currentCycle < workout.cycles) {
                    newPhase = Phase.REST
                    nextDuration = workout.restDuration
                } else {
                    if (currentState.currentSet < workout.sets) {
                        newPhase = Phase.REST_BETWEEN_SETS
                        nextDuration = workout.restBetweenSets
                        newCycle = 1
                        newSet++
                    } else {
                        newPhase = Phase.COOLDOWN
                        nextDuration = workout.cooldownDuration
                    }
                }
            }
            Phase.REST -> {
                newPhase = Phase.WORK
                nextDuration = workout.workDuration
                newCycle++
            }
            Phase.REST_BETWEEN_SETS -> {
                newPhase = Phase.WORK
                nextDuration = workout.workDuration
            }
            Phase.COOLDOWN -> {
                newPhase = Phase.FINISHED
                nextDuration = 0
                countDownTimer?.cancel()
                _timerState.value = _timerState.value.copy(isRunning = false)
                return
            }
            Phase.FINISHED -> {
                return
            }
        }

        phaseTimeMillis = nextDuration * 1000L
        
        _timerState.value = _timerState.value.copy(
            phase = newPhase,
            timeRemaining = nextDuration,
            currentCycle = newCycle,
            currentSet = newSet,
            progress = 0f
        )

        if (newPhase != Phase.FINISHED) {
            startTimer()
        }
    }

    private fun calculateTotalTime(workout: Workout): Int {
        // Warmup
        var total = workout.warmupDuration
        
        // Work + Rest for each cycle across all sets
        total += workout.sets * (
            (workout.workDuration + workout.restDuration) * workout.cycles
            - workout.restDuration // Subtract the last rest period of each set
        )
        
        // Rest between sets
        if (workout.sets > 1) {
            total += (workout.sets - 1) * workout.restBetweenSets
        }
        
        // Cooldown
        total += workout.cooldownDuration
        
        return total
    }
} 