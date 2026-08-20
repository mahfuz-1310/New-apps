package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.PasswordEntity
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricBottomNav
import com.example.ui.theme.GeometricPrimaryContainer
import com.example.ui.theme.GeometricOnPrimaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordListScreen(
    viewModel: PasswordViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val passwords by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Passwords", 
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = GeometricBottomNav,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.VpnKey, contentDescription = "Passwords") },
                    label = { Text("Passwords", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeometricOnPrimaryContainer,
                        indicatorColor = GeometricPrimaryContainer
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHistory,
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History", fontWeight = FontWeight.Medium) },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontWeight = FontWeight.Medium) },
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = GeometricPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Password")
            }
        }
    ) { paddingValues ->
        if (passwords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Empty",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No passwords saved yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tap the + button to add one.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(passwords) { password ->
                    PasswordItem(password = password, onDelete = { viewModel.deletePassword(it) })
                }
            }
        }
    }
}

@Composable
fun PasswordItem(password: PasswordEntity, onDelete: (PasswordEntity) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: edit */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                shape = RoundedCornerShape(percent = 50),
                color = GeometricPrimaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = null,
                        tint = GeometricOnPrimaryContainer
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = password.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = password.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
