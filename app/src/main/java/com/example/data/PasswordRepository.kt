package com.example.data

import kotlinx.coroutines.flow.Flow

class PasswordRepository(
    private val passwordDao: PasswordDao,
    private val generatedPasswordDao: GeneratedPasswordDao
) {
    val allPasswords: Flow<List<PasswordEntity>> = passwordDao.getAllPasswords()
    val allGeneratedPasswords: Flow<List<GeneratedPasswordEntity>> = generatedPasswordDao.getAllGeneratedPasswords()

    suspend fun insert(password: PasswordEntity) {
        passwordDao.insertPassword(password)
    }

    suspend fun delete(password: PasswordEntity) {
        passwordDao.deletePassword(password)
    }
    
    suspend fun insertGenerated(password: GeneratedPasswordEntity) {
        generatedPasswordDao.insertGeneratedPassword(password)
    }
    
    suspend fun clearGeneratedHistory() {
        generatedPasswordDao.clearHistory()
    }

    suspend fun deleteGenerated(password: GeneratedPasswordEntity) {
        generatedPasswordDao.deleteGeneratedPassword(password)
    }
}
