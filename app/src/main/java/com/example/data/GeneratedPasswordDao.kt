package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneratedPasswordDao {
    @Query("SELECT * FROM generated_passwords ORDER BY timestamp DESC")
    fun getAllGeneratedPasswords(): Flow<List<GeneratedPasswordEntity>>

    @Insert
    suspend fun insertGeneratedPassword(password: GeneratedPasswordEntity)

    @Query("DELETE FROM generated_passwords")
    suspend fun clearHistory()
}
