package com.piea.student.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = PieaBrown,
    onPrimary = PieaOnPrimary,
    secondary = PieaGold,
    onSecondary = PieaTextPrimaryLight,
    tertiary = PieaGoldLight,
    background = PieaBackgroundLight,
    surface = PieaSurfaceLight,
    surfaceVariant = PieaCream,
    error = PieaError,
    onBackground = PieaTextPrimaryLight,
    onSurface = PieaTextPrimaryLight
)

private val DarkColors = darkColorScheme(
    primary = PieaGold,
    onPrimary = PieaTextPrimaryLight,
    secondary = PieaGoldLight,
    onSecondary = PieaTextPrimaryLight,
    tertiary = PieaCream,
    background = PieaBackgroundDark,
    surface = PieaSurfaceDark,
    surfaceVariant = PieaBrownDark,
    error = PieaError,
    onBackground = PieaTextPrimaryDark,
    onSurface = PieaTextPrimaryDark
)

@Composable
fun PieaStudentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // keep brand colors consistent by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PieaTypography,
        content = content
    )
}
