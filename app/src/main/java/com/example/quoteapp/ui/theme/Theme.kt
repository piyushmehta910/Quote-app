package com.example.quoteapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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
    error = ErrorColor,
    onError = SurfaceLight
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
    error = ErrorColor,
    onError = BackgroundDark
)

@Composable
fun QuoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
