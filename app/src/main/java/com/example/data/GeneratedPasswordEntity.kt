package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_passwords")
data class GeneratedPasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val passwordValue: String,
    val timestamp: Long = System.currentTimeMillis()
)
