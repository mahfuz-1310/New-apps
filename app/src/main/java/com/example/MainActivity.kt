package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.PasswordDatabase
import com.example.data.PasswordRepository
import com.example.ui.AddPasswordScreen
import com.example.ui.PasswordListScreen
import com.example.ui.PasswordViewModel
import com.example.ui.PasswordViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    val database = PasswordDatabase.getDatabase(this)
    val repository = PasswordRepository(database.passwordDao(), database.generatedPasswordDao())

    setContent {
      MyApplicationTheme {
        val navController = rememberNavController()
        val viewModel: PasswordViewModel = viewModel(
            factory = PasswordViewModelFactory(repository)
        )

        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                PasswordListScreen(
                    viewModel = viewModel,
                    onNavigateToAdd = { navController.navigate("add") },
                    onNavigateToHistory = {
                        navController.navigate("history") {
                            popUpTo("list") { inclusive = true }
                        }
                    }
                )
            }
            composable("history") {
                com.example.ui.PasswordHistoryScreen(
                    viewModel = viewModel,
                    onNavigateToPasswords = {
                        navController.navigate("list") {
                            popUpTo("history") { inclusive = true }
                        }
                    }
                )
            }
            composable("add") {
                AddPasswordScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
      }
    }
  }
}
