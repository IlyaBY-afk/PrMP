package com.example.tabatatimer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabatatimer.R
import com.example.tabatatimer.data.model.Workout

class WorkoutAdapter(
    private val onStartClick: (Workout) -> Unit,
    private val onEditClick: (Workout) -> Unit,
    private val onDeleteClick: (Workout) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.WorkoutViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.text_workout_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_workout_description)
        private val detailsTextView: TextView = itemView.findViewById(R.id.text_workout_details)
        private val startButton: Button = itemView.findViewById(R.id.button_start)
        private val editButton: ImageButton = itemView.findViewById(R.id.button_edit)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete)

        fun bind(workout: Workout) {
            nameTextView.text = workout.name
            
            if (workout.description.isNotEmpty()) {
                descriptionTextView.text = workout.description
                descriptionTextView.visibility = View.VISIBLE
            } else {
                descriptionTextView.visibility = View.GONE
            }

            val details = "${workout.cycles} cycles • ${workout.workDuration}s work • ${workout.restDuration}s rest"
            detailsTextView.text = details

            startButton.setOnClickListener { onStartClick(workout) }
            editButton.setOnClickListener { onEditClick(workout) }
            deleteButton.setOnClickListener { onDeleteClick(workout) }
        }
    }

    private class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }
} 