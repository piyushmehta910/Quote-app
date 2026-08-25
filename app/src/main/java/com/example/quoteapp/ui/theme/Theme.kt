package com.example.quoteapp.ui.theme

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

var isDarkModeOverride by mutableStateOf<Boolean?>(null)

val isAppDarkTheme: Boolean
    @Composable
    get() = isDarkModeOverride ?: isSystemInDarkTheme()

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = SurfaceLight,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = OnBackgroundLight,
    secondary = Secondary,
    onSecondary = SurfaceLight,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnBackgroundLight,
    tertiary = Tertiary,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineLight.copy(alpha = 0.5f),
    error = ErrorColor,
    onError = SurfaceLight,
    inverseSurface = OnBackgroundLight,
    inverseOnSurface = SurfaceLight,
    inversePrimary = PrimaryLight,
    surfaceTint = Primary,
    scrim = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = BackgroundDark,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = OnBackgroundDark,
    secondary = Secondary,
    onSecondary = BackgroundDark,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = OnBackgroundDark,
    tertiary = Tertiary,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineDark.copy(alpha = 0.5f),
    error = ErrorColor,
    onError = BackgroundDark,
    inverseSurface = OnBackgroundDark,
    inverseOnSurface = SurfaceDark,
    inversePrimary = Primary,
    surfaceTint = PrimaryLight,
    scrim = Color.Black
)

@Composable
fun QuoteAppTheme(
    darkTheme: Boolean = isAppDarkTheme,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
