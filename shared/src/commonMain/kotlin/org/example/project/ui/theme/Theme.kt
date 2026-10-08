package org.example.project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB9F6CA),
    onPrimaryContainer = Color(0xFF00210B),
    secondary = Color(0xFF00838F),
    tertiary = Color(0xFF6A1B9A),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF69F0AE),
    onPrimary = Color(0xFF00391A),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFB9F6CA),
    secondary = Color(0xFF4DD0E1),
    tertiary = Color(0xFFCE93D8),
)

/** Um único Boolean (darkTheme) troca o visual do app inteiro, nas 3 plataformas. */
@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
