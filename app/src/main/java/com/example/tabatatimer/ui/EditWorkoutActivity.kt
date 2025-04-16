package com.example.tabatatimer.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tabatatimer.R
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.viewmodel.WorkoutViewModel
import com.google.android.material.textfield.TextInputEditText

class EditWorkoutActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WORKOUT_ID = "com.example.tabatatimer.WORKOUT_ID"
    }

    private lateinit var workoutViewModel: WorkoutViewModel
    
    private lateinit var nameEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var warmupEditText: TextInputEditText
    private lateinit var workEditText: TextInputEditText
    private lateinit var restEditText: TextInputEditText
    private lateinit var cyclesEditText: TextInputEditText
    private lateinit var setsEditText: TextInputEditText
    private lateinit var restBetweenSetsEditText: TextInputEditText
    private lateinit var cooldownEditText: TextInputEditText
    
    private var workoutId: Long = 0
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_workout)
        
        // Initialize ViewModel
        workoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        
        // Initialize views
        nameEditText = findViewById(R.id.edit_workout_name)
        descriptionEditText = findViewById(R.id.edit_workout_description)
        warmupEditText = findViewById(R.id.edit_warmup_duration)
        workEditText = findViewById(R.id.edit_work_duration)
        restEditText = findViewById(R.id.edit_rest_duration)
        cyclesEditText = findViewById(R.id.edit_cycles)
        setsEditText = findViewById(R.id.edit_sets)
        restBetweenSetsEditText = findViewById(R.id.edit_rest_between_sets)
        cooldownEditText = findViewById(R.id.edit_cooldown_duration)
        
        val saveButton: Button = findViewById(R.id.button_save)
        val cancelButton: Button = findViewById(R.id.button_cancel)
        
        // Check if editing existing workout
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            isEditMode = true
            workoutId = intent.getLongExtra(EXTRA_WORKOUT_ID, 0)
            
            // Set title
            title = getString(R.string.edit_workout)
            
            // Load workout data
            workoutViewModel.getWorkoutById(workoutId).observe(this) { workout ->
                if (workout != null) {
                    populateFields(workout)
                }
            }
        } else {
            // Creating new workout
            title = getString(R.string.create_workout)
        }
        
        // Setup click listeners
        saveButton.setOnClickListener {
            saveWorkout()
        }
        
        cancelButton.setOnClickListener {
            finish()
        }
    }
    
    private fun populateFields(workout: Workout) {
        nameEditText.setText(workout.name)
        descriptionEditText.setText(workout.description)
        warmupEditText.setText(workout.warmupDuration.toString())
        workEditText.setText(workout.workDuration.toString())
        restEditText.setText(workout.restDuration.toString())
        cyclesEditText.setText(workout.cycles.toString())
        setsEditText.setText(workout.sets.toString())
        restBetweenSetsEditText.setText(workout.restBetweenSets.toString())
        cooldownEditText.setText(workout.cooldownDuration.toString())
    }
    
    private fun saveWorkout() {
        val name = nameEditText.text.toString().trim()
        
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.workout_name_required, Toast.LENGTH_SHORT).show()
            return
        }
        
        val description = descriptionEditText.text.toString().trim()
        
        val warmupDuration = warmupEditText.text.toString().toIntOrNull() ?: 10
        val workDuration = workEditText.text.toString().toIntOrNull() ?: 20
        val restDuration = restEditText.text.toString().toIntOrNull() ?: 10
        val cycles = cyclesEditText.text.toString().toIntOrNull() ?: 8
        val sets = setsEditText.text.toString().toIntOrNull() ?: 1
        val restBetweenSets = restBetweenSetsEditText.text.toString().toIntOrNull() ?: 60
        val cooldownDuration = cooldownEditText.text.toString().toIntOrNull() ?: 10
        
        val workout = Workout(
            id = if (isEditMode) workoutId else 0,
            name = name,
            description = description,
            warmupDuration = warmupDuration,
            workDuration = workDuration,
            restDuration = restDuration,
            cycles = cycles,
            sets = sets,
            restBetweenSets = restBetweenSets,
            cooldownDuration = cooldownDuration
        )
        
        if (isEditMode) {
            workoutViewModel.update(workout)
        } else {
            workoutViewModel.insert(workout)
        }
        
        finish()
    }
} 