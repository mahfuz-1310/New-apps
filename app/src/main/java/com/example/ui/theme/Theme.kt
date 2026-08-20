package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)

private val LightColorScheme =
  lightColorScheme(
    primary = GeometricPrimary,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = GeometricPrimaryContainer,
    onPrimaryContainer = GeometricOnPrimaryContainer,
    background = GeometricBackground,
    onBackground = GeometricOnBackground,
    surface = GeometricBackground,
    onSurface = GeometricOnBackground,
    surfaceVariant = GeometricSurfaceVariant,
    secondaryContainer = GeometricSecondaryContainer,
    outline = androidx.compose.ui.graphics.Color(0xFF9CA3AF) // gray-400 equivalent for borders
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabling dynamic color to ensure Geometric theme always shows
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
