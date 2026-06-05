package com.spartanai.nova.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NovaGreen,
    secondary = NovaBlue,
    tertiary = NovaRed,
    background = NovaBlack,
    surface = NovaDarkGray,
    onPrimary = NovaBlack,
    onSecondary = NovaBlack,
    onTertiary = NovaWhite,
    onBackground = NovaGreen,
    onSurface = NovaWhite,
)

@Composable
fun NOVATheme(
    darkTheme: Boolean = true, // Force dark theme for offensive app
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}