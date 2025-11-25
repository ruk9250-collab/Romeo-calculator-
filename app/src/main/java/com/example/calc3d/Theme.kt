package com.example.calc3d

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    background = Color(0xFFF3F7FB),
    onBackground = Color(0xFF0B1220),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0B1220),
    error = Color(0xFFDB4437),
    surfaceVariant = Color(0xFFE7F0FF),
    primaryContainer = Color(0xFFCEE7FF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4CC2FF),
    onPrimary = Color.Black,
    background = Color(0xFF071124),
    onBackground = Color(0xFFEFF6FF),
    surface = Color(0xFF0D1B2A),
    onSurface = Color(0xFFEFF6FF),
    error = Color(0xFFEF9A9A),
    surfaceVariant = Color(0xFF0A2436),
    primaryContainer = Color(0xFF063B5A)
)

val lightPalette get() = LightColors
val darkPalette get() = DarkColors
