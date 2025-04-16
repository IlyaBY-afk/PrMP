package com.example.tabatatimer.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.R
import com.example.tabatatimer.data.AppDatabase
import com.example.tabatatimer.data.model.Workout
import com.example.tabatatimer.data.repository.WorkoutRepository
import com.example.tabatatimer.ui.adapter.WorkoutAdapter
import com.example.tabatatimer.util.DataInitializer
import com.example.tabatatimer.viewmodel.WorkoutViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var adapter: WorkoutAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var noWorkoutsTextView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_workouts)
        noWorkoutsTextView = findViewById(R.id.text_no_workouts)
        val fabAddWorkout: FloatingActionButton = findViewById(R.id.fab_add_workout)
        
        // Setup RecyclerView
        adapter = WorkoutAdapter(
            onStartClick = { startWorkout(it) },
            onEditClick = { editWorkout(it) },
            onDeleteClick = { confirmDeleteWorkout(it) }
        )
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        
        // Setup ViewModel
        workoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        
        // Initialize sample data if first run
        initializeSampleData()
        
        // Observe workouts
        workoutViewModel.allWorkouts.observe(this) { workouts ->
            adapter.submitList(workouts)
            
            if (workouts.isEmpty()) {
                recyclerView.visibility = View.GONE
                noWorkoutsTextView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                noWorkoutsTextView.visibility = View.GONE
            }
        }
        
        // Setup FAB click listener
        fabAddWorkout.setOnClickListener {
            val intent = Intent(this, EditWorkoutActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun initializeSampleData() {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = WorkoutRepository(database.workoutDao())
        val dataInitializer = DataInitializer(applicationContext, repository)
        
        lifecycleScope.launch {
            dataInitializer.initializeSampleDataIfNeeded()
        }
    }
    
    private fun startWorkout(workout: Workout) {
        val intent = Intent(this, TimerActivity::class.java).apply {
            putExtra(TimerActivity.EXTRA_WORKOUT_ID, workout.id)
        }
        startActivity(intent)
    }
    
    private fun editWorkout(workout: Workout) {
        val intent = Intent(this, EditWorkoutActivity::class.java).apply {
            putExtra(EditWorkoutActivity.EXTRA_WORKOUT_ID, workout.id)
        }
        startActivity(intent)
    }
    
    private fun confirmDeleteWorkout(workout: Workout) {
        AlertDialog.Builder(this)
            .setTitle(workout.name)
            .setMessage(R.string.confirm_delete)
            .setPositiveButton(R.string.yes) { _, _ ->
                workoutViewModel.delete(workout)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
} 