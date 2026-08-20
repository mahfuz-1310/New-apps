package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.GeneratedPasswordEntity
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricPrimaryContainer
import com.example.ui.theme.GeometricOnPrimaryContainer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordHistoryScreen(
    viewModel: PasswordViewModel,
    onNavigateToPasswords: () -> Unit
) {
    val history by viewModel.generatedHistoryState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Generated History", 
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    if (history.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearGeneratedHistory() }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear History")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = com.example.ui.theme.GeometricBottomNav,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToPasswords,
                    icon = { Icon(Icons.Default.VpnKey, contentDescription = "Passwords") },
                    label = { Text("Passwords", fontWeight = FontWeight.Medium) },
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = GeometricOnPrimaryContainer,
                        indicatorColor = GeometricPrimaryContainer
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontWeight = FontWeight.Medium) },
                )
            }
        }
    ) { paddingValues ->
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Empty History",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No passwords generated yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
                items(history) { passwordEntity ->
                    GeneratedPasswordItem(
                        passwordEntity = passwordEntity, 
                        onCopy = { copiedText ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Password copied to clipboard")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GeneratedPasswordItem(
    passwordEntity: GeneratedPasswordEntity,
    onCopy: (String) -> Unit
) {
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(passwordEntity.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = passwordEntity.passwordValue,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            IconButton(
                onClick = {
                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(passwordEntity.passwordValue))
                    onCopy(passwordEntity.passwordValue)
                },
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = GeometricPrimaryContainer,
                    contentColor = GeometricOnPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy password",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
