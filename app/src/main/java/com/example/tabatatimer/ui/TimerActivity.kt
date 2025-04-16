package com.example.tabatatimer.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tabatatimer.R
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.timer.TabataTimer
import com.example.tabatatimer.viewmodel.TimerViewModel
import com.example.tabatatimer.viewmodel.WorkoutViewModel

class TimerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORKOUT_ID = "com.example.tabatatimer.WORKOUT_ID"
    }

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var timerViewModel: TimerViewModel
    
    private lateinit var workoutNameTextView: TextView
    private lateinit var phaseTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var cycleInfoTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var toggleButton: Button
    private lateinit var resetButton: Button
    private lateinit var backgroundView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        
        // Initialize ViewModels
        workoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        
        // Initialize views
        workoutNameTextView = findViewById(R.id.text_workout_name)
        phaseTextView = findViewById(R.id.text_phase)
        timeTextView = findViewById(R.id.text_time)
        cycleInfoTextView = findViewById(R.id.text_cycle_info)
        progressBar = findViewById(R.id.progress_circular)
        toggleButton = findViewById(R.id.button_toggle)
        resetButton = findViewById(R.id.button_reset)
        backgroundView = findViewById(R.id.background_view)
        
        // Get workout ID from intent
        val workoutId = intent.getLongExtra(EXTRA_WORKOUT_ID, 0)
        if (workoutId == 0L) {
            finish()
            return
        }
        
        // Load workout from database
        workoutViewModel.getWorkoutById(workoutId).observe(this) { workout ->
            if (workout != null) {
                setupWorkout(workout)
            } else {
                finish()
            }
        }
        
        // Set up button click listeners
        toggleButton.setOnClickListener {
            timerViewModel.toggleTimer()
        }
        
        resetButton.setOnClickListener {
            timerViewModel.resetTimer()
        }
        
        // Observe timer state
        observeTimerState()
    }
    
    private fun setupWorkout(workout: Workout) {
        workoutNameTextView.text = workout.name
        timerViewModel.setWorkout(workout)
    }
    
    private fun observeTimerState() {
        timerViewModel.timeRemaining.observe(this) { seconds ->
            timeTextView.text = timerViewModel.formatTime(seconds)
        }
        
        timerViewModel.progress.observe(this) { progress ->
            progressBar.progress = (progress * 100).toInt()
        }
        
        timerViewModel.currentPhase.observe(this) { phase ->
            val phaseString = when (phase) {
                TabataTimer.Phase.WARMUP -> getString(R.string.phase_warmup)
                TabataTimer.Phase.WORK -> getString(R.string.phase_work)
                TabataTimer.Phase.REST -> getString(R.string.phase_rest)
                TabataTimer.Phase.REST_BETWEEN_SETS -> getString(R.string.phase_rest_between_sets)
                TabataTimer.Phase.COOLDOWN -> getString(R.string.phase_cooldown)
                TabataTimer.Phase.FINISHED -> getString(R.string.phase_finished)
            }
            phaseTextView.text = phaseString
            
            val color = when (phase) {
                TabataTimer.Phase.WARMUP -> getColor(R.color.phase_warmup)
                TabataTimer.Phase.WORK -> getColor(R.color.phase_work)
                TabataTimer.Phase.REST -> getColor(R.color.phase_rest)
                TabataTimer.Phase.REST_BETWEEN_SETS -> getColor(R.color.phase_rest_between_sets)
                TabataTimer.Phase.COOLDOWN -> getColor(R.color.phase_cooldown)
                TabataTimer.Phase.FINISHED -> getColor(R.color.phase_finished)
            }
            backgroundView.setBackgroundColor(color)
        }
        
        timerViewModel.timerState.observe(this) { state ->
            val cycleText = "${getString(R.string.cycle)} ${state.currentCycle} of ${state.totalCycles} | " +
                "${getString(R.string.set)} ${state.currentSet} of ${state.totalSets}"
            cycleInfoTextView.text = cycleText
        }
        
        timerViewModel.isRunning.observe(this) { isRunning ->
            toggleButton.text = if (isRunning) {
                getString(R.string.pause)
            } else {
                getString(R.string.resume)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerViewModel.pauseTimer() // Make sure timer stops when activity is destroyed
    }
} 