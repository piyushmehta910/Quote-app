package com.example.quoteapp.data

import com.example.quoteapp.model.GradientType
import com.example.quoteapp.model.PatternType
import com.example.quoteapp.model.QuoteBackground

object BackgroundLibrary {

    val solidColors: List<QuoteBackground.SolidColor> = listOf(
        QuoteBackground.SolidColor(0xFF000000),
        QuoteBackground.SolidColor(0xFFFFFFFF),
        QuoteBackground.SolidColor(0xFF1A1A2E),
        QuoteBackground.SolidColor(0xFF16213E),
        QuoteBackground.SolidColor(0xFF0F3460),
        QuoteBackground.SolidColor(0xFF533483),
        QuoteBackground.SolidColor(0xFF2C3E50),
        QuoteBackground.SolidColor(0xFF1B262C),
        QuoteBackground.SolidColor(0xFF0A1929),
        QuoteBackground.SolidColor(0xFF2D2D3A),
        QuoteBackground.SolidColor(0xFF3C1642),
        QuoteBackground.SolidColor(0xFF0B0C10),
        QuoteBackground.SolidColor(0xFFF5F5DC),
        QuoteBackground.SolidColor(0xFFFAF0E6),
        QuoteBackground.SolidColor(0xFF2E4057),
        QuoteBackground.SolidColor(0xFF1B1B2F),
    )

    val gradients: List<QuoteBackground.Gradient> = listOf(
        QuoteBackground.Gradient(listOf(0xFF667EEA, 0xFF764BA2), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFFF093FB, 0xFFF5576C), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF4FACFE, 0xFF00F2FE), 90f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF43E97B, 0xFF38F9D7), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFFFA709A, 0xFFFEE140), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFFA18CD1, 0xFFFBC2EB), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF667EEA, 0xFF764BA2), 0f, GradientType.RADIAL),
        QuoteBackground.Gradient(listOf(0xFF0C0C1D, 0xFF1A1A3E, 0xFF2D1B69), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF141E30, 0xFF243B55), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF0F2027, 0xFF2C5364), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF200122, 0xFF6F0000), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF1F1C2C, 0xFF928DAB), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFFFC466B, 0xFF3F5EFB), 135f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFF3C1053, 0xFFAD5389), 180f, GradientType.LINEAR),
        QuoteBackground.Gradient(listOf(0xFFC9D6FF, 0xFFE2E2E2), 135f, GradientType.LINEAR),
    )

    val patterns: List<QuoteBackground.Programmatic> = listOf(
        QuoteBackground.Programmatic(PatternType.NOISE, 0xFF1A1A2E, 0xFF16213E),
        QuoteBackground.Programmatic(PatternType.PAPER, 0xFFF5F0EB, 0xFFD4C5B2),
        QuoteBackground.Programmatic(PatternType.GEOMETRIC, 0xFF0A1929, 0xFF1E88E5),
        QuoteBackground.Programmatic(PatternType.SOFT_GRADIENT, 0xFF1A1A2E, 0xFF533483),
        QuoteBackground.Programmatic(PatternType.DARK_TEXTURE, 0xFF0B0C10, 0xFFC5C6C7),
        QuoteBackground.Programmatic(PatternType.ABSTRACT_GRADIENT, 0xFF1B262C, 0xFF0F4C75),
        QuoteBackground.Programmatic(PatternType.MINIMAL, 0xFFFFFFFF, 0xFF1A1A2E),
        QuoteBackground.Programmatic(PatternType.NOISE, 0xFF2D2D3A, 0xFF4A4A5A),
        QuoteBackground.Programmatic(PatternType.GEOMETRIC, 0xFF3C1642, 0xFFE94560),
        QuoteBackground.Programmatic(PatternType.PAPER, 0xFFFAF0E6, 0xFF8B7355),
        QuoteBackground.Programmatic(PatternType.SOFT_GRADIENT, 0xFF0F3460, 0xFF533483),
        QuoteBackground.Programmatic(PatternType.DARK_TEXTURE, 0xFF141E30, 0xFF243B55),
    )

    val all: List<QuoteBackground>
        get() = solidColors + gradients + patterns

    fun getById(id: String): QuoteBackground? = all.find { it.id == id }
}
