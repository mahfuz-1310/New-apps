package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GeometricPrimary
import com.example.ui.theme.GeometricPrimaryContainer
import com.example.ui.theme.GeometricOnPrimaryContainer

import kotlinx.coroutines.launch

enum class PasswordStrength(val label: String, val color: Color, val segments: Int) {
    NONE("Enter password", Color.Gray, 0),
    WEAK("Weak", Color(0xFFE53935), 1),
    FAIR("Fair", Color(0xFFFFB300), 2),
    GOOD("Good", Color(0xFF43A047), 3),
    STRONG("Strong", Color(0xFF005FB0), 4)
}

fun calculateStrength(password: String): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength.NONE
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isLowerCase() } && password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    
    return when {
        password.length < 6 -> PasswordStrength.WEAK
        score <= 1 -> PasswordStrength.WEAK
        score == 2 -> PasswordStrength.FAIR
        score == 3 -> PasswordStrength.GOOD
        else -> PasswordStrength.STRONG
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = calculateStrength(password)
    
    Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, start = 4.dp, end = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..4) {
                val segmentColor = if (i <= strength.segments) strength.color else MaterialTheme.colorScheme.surfaceVariant
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(segmentColor)
                )
            }
        }
        Text(
            text = strength.label,
            style = MaterialTheme.typography.bodySmall,
            color = if (strength == PasswordStrength.NONE) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else strength.color,
            modifier = Modifier.padding(top = 6.dp).align(Alignment.End),
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        val hasLength = password.length >= 8
        val hasUpper = password.any { it.isUpperCase() }
        val hasLower = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }
        
        ValidationRow("At least 8 characters", hasLength)
        ValidationRow("Uppercase & lowercase", hasUpper && hasLower)
        ValidationRow("Contains a number", hasDigit)
        ValidationRow("Contains a symbol", hasSymbol)
    }
}

@Composable
fun ValidationRow(text: String, isValid: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isValid) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Valid",
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF43A047)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(2.dp)
                    .background(Color.Transparent, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (isValid) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordScreen(
    viewModel: PasswordViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text("New Password", fontWeight = FontWeight.Medium) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        title = ""
                        username = ""
                        password = ""
                        passwordVisible = false
                    }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear all fields")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Website or app", fontWeight = FontWeight.Medium) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = GeometricPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", fontWeight = FontWeight.Medium) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = GeometricPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )

            Box(contentAlignment = Alignment.CenterEnd) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", fontWeight = FontWeight.Medium) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedIndicatorColor = GeometricPrimary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }, modifier = Modifier.padding(end = 96.dp)) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                
                val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                val context = androidx.compose.ui.platform.LocalContext.current
                
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (password.isNotEmpty()) {
                                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(password))
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Password copied to clipboard")
                                }
                            }
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy, 
                            contentDescription = "Copy password",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            val newPassword = generateStrongPassword()
                            password = newPassword
                            passwordVisible = true
                            viewModel.logGeneratedPassword(newPassword)
                        },
                        modifier = Modifier.size(40.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = GeometricPrimaryContainer,
                            contentColor = GeometricOnPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh, 
                            contentDescription = "Generate password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            PasswordStrengthIndicator(password = password)
            
            // Generator Settings Card mock
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Generator settings", fontWeight = FontWeight.Medium)
                        val currentStrength = calculateStrength(password)
                        val badgeColor = if (currentStrength == PasswordStrength.NONE) GeometricPrimaryContainer else currentStrength.color.copy(alpha = 0.2f)
                        val badgeTextColor = if (currentStrength == PasswordStrength.NONE) GeometricOnPrimaryContainer else currentStrength.color
                        
                        Surface(
                            shape = RoundedCornerShape(percent = 50),
                            color = badgeColor,
                        ) {
                            Text(
                                if (currentStrength == PasswordStrength.NONE) "SETTINGS" else currentStrength.label.uppercase(), 
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor
                            )
                        }
                    }
                    
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Length", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text("16 characters", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = GeometricPrimary)
                        }
                        Slider(value = 16f, onValueChange = {}, valueRange = 8f..32f, colors = SliderDefaults.colors(
                            thumbColor = GeometricPrimary,
                            activeTrackColor = GeometricPrimary
                        ))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(50),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Medium, color = GeometricPrimary)
                }
                Button(
                    onClick = {
                        if (title.isNotBlank() && username.isNotBlank() && password.isNotBlank()) {
                            viewModel.addPassword(title, username, password)
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.weight(1.5f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GeometricPrimary, contentColor = Color.White),
                    shape = RoundedCornerShape(50),
                    enabled = title.isNotBlank() && username.isNotBlank() && password.isNotBlank(),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Save password", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

private fun generateStrongPassword(): String {
    val uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercase = "abcdefghijklmnopqrstuvwxyz"
    val numbers = "0123456789"
    val specials = "!@#\$%^&*"
    
    val allChars = uppercase + lowercase + numbers + specials
    val length = 16
    
    val password = java.lang.StringBuilder()
    // Guarantee at least one of each
    password.append(uppercase.random())
    password.append(lowercase.random())
    password.append(numbers.random())
    password.append(specials.random())
    
    // Fill the rest
    for (i in 0 until length - 4) {
        password.append(allChars.random())
    }
    
    // Shuffle the result
    return password.toString().toList().shuffled().joinToString("")
}
