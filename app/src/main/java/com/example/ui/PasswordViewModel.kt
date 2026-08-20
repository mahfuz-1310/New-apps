package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PasswordEntity
import com.example.data.PasswordRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.data.GeneratedPasswordEntity

class PasswordViewModel(private val repository: PasswordRepository) : ViewModel() {

    val uiState: StateFlow<List<PasswordEntity>> = repository.allPasswords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    val generatedHistoryState: StateFlow<List<GeneratedPasswordEntity>> = repository.allGeneratedPasswords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPassword(title: String, username: String, passwordHash: String) {
        viewModelScope.launch {
            repository.insert(PasswordEntity(title = title, username = username, passwordHash = passwordHash))
        }
    }

    fun deletePassword(password: PasswordEntity) {
        viewModelScope.launch {
            repository.delete(password)
        }
    }
    
    fun logGeneratedPassword(password: String) {
        viewModelScope.launch {
            repository.insertGenerated(GeneratedPasswordEntity(passwordValue = password))
        }
    }
    
    fun clearGeneratedHistory() {
        viewModelScope.launch {
            repository.clearGeneratedHistory()
        }
    }
}

class PasswordViewModelFactory(private val repository: PasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
