package com.example.tabatatimer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var description: String = "",
    var warmupDuration: Int = 10, // in seconds
    var workDuration: Int = 20, // in seconds
    var restDuration: Int = 10, // in seconds
    var cycles: Int = 8,
    var sets: Int = 1,
    var restBetweenSets: Int = 60, // in seconds
    var cooldownDuration: Int = 10, // in seconds
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) 