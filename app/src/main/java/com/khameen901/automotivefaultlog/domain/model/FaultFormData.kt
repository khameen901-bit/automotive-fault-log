package com.khameen901.automotivefaultlog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueDark,
    secondary = AccentTeal,
    tertiary = WarningAmber,
    background = SurfaceLight,
    surface = Color.White,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF101821),
    onSurface = Color(0xFF101821)
)

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryBlueDark,
    secondary = AccentTeal,
    tertiary = WarningAmber,
    background = SurfaceDark,
    surface = Color(0xFF1A2430),
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun AutomotiveFaultLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
