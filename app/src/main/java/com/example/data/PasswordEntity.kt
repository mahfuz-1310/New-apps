package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val username: String,
    val passwordHash: String, // Keeping it simple for UI, usually would be encrypted in a real manager
    val timestamp: Long = System.currentTimeMillis()
)
