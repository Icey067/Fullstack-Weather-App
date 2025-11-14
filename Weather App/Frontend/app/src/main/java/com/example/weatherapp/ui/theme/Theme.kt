package com.example.weatherapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1.dark theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // A purple color for buttons
    secondary = Color(0xFF03DAC5), // A teal color for accents
    background = Color(0xFF121212), // This is the main dark background color
    surface = Color(0xFF1E1E1E), // Color for cards
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White, // Text on the dark background will be white
    onSurface = Color.White, // Text on cards will be white
    onSurfaceVariant = Color.Gray, // For hint text
    error = Color(0xFFCF6679)
)

// 2. Create the theme composable
@Composable
fun WeatherAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme, // 3. Apply the dark color scheme
        typography = MaterialTheme.typography,
        content = content
    )
}