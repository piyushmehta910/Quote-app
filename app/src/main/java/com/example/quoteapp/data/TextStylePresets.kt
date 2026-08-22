package com.example.quoteapp.data

import com.example.quoteapp.model.FontFamily
import com.example.quoteapp.model.FontWeight
import com.example.quoteapp.model.TextAlign
import com.example.quoteapp.model.TextSettings
import com.example.quoteapp.model.TextStylePreset

object TextStylePresets {

    val presets: List<TextStylePreset> = listOf(
        TextStylePreset(
            id = "minimal",
            displayName = "Minimal",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 42f,
                fontWeight = FontWeight.LIGHT,
                color = 0xFFFFFFFF,
                lineHeight = 1.5f,
                letterSpacing = 0.02f
            )
        ),
        TextStylePreset(
            id = "bold",
            displayName = "Bold",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 52f,
                isBold = true,
                color = 0xFFFFFFFF,
                lineHeight = 1.3f
            )
        ),
        TextStylePreset(
            id = "elegant",
            displayName = "Elegant",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 44f,
                fontWeight = FontWeight.NORMAL,
                color = 0xFFD4AF37,
                lineHeight = 1.5f,
                letterSpacing = 0.05f,
                isItalic = true
            )
        ),
        TextStylePreset(
            id = "modern",
            displayName = "Modern",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 40f,
                fontWeight = FontWeight.MEDIUM,
                color = 0xFFFFFFFF,
                lineHeight = 1.4f,
                letterSpacing = -0.01f
            )
        ),
        TextStylePreset(
            id = "cinematic",
            displayName = "Cinematic",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 56f,
                fontWeight = FontWeight.BOLD,
                color = 0xFFFFFFFF,
                isBold = true,
                lineHeight = 1.2f,
                letterSpacing = 0.08f,
                shadowEnabled = true,
                shadowRadius = 8f,
                shadowColor = 0x88000000L,
                shadowDx = 3f,
                shadowDy = 3f
            )
        ),
        TextStylePreset(
            id = "typewriter",
            displayName = "Typewriter",
            settings = TextSettings(
                fontFamily = FontFamily.MONOSPACE,
                fontSize = 36f,
                color = 0xFFE0E0E0,
                lineHeight = 1.6f,
                letterSpacing = 0.03f
            )
        ),
        TextStylePreset(
            id = "serif",
            displayName = "Serif",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 46f,
                color = 0xFFFFFFFF,
                lineHeight = 1.5f,
                letterSpacing = 0.02f
            )
        ),
        TextStylePreset(
            id = "handwritten",
            displayName = "Handwritten",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 40f,
                isItalic = true,
                color = 0xFFE8D5B7,
                lineHeight = 1.7f,
                letterSpacing = 0.01f
            )
        ),
        TextStylePreset(
            id = "neon",
            displayName = "Neon",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 48f,
                fontWeight = FontWeight.BOLD,
                color = 0xFF00FFFF,
                isBold = true,
                lineHeight = 1.3f,
                shadowEnabled = true,
                shadowRadius = 12f,
                shadowColor = 0xAA00FFFFL,
                shadowDx = 0f,
                shadowDy = 0f
            )
        ),
        TextStylePreset(
            id = "luxury",
            displayName = "Luxury",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 50f,
                fontWeight = FontWeight.NORMAL,
                color = 0xFFD4AF37,
                lineHeight = 1.4f,
                letterSpacing = 0.15f
            )
        ),
        TextStylePreset(
            id = "newspaper",
            displayName = "Newspaper",
            settings = TextSettings(
                fontFamily = FontFamily.SERIF,
                fontSize = 38f,
                color = 0xFF1A1A1A,
                lineHeight = 1.4f,
                letterSpacing = 0.01f,
                isBold = true
            )
        ),
        TextStylePreset(
            id = "gradient",
            displayName = "Gradient",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 48f,
                fontWeight = FontWeight.BOLD,
                color = 0xFFFFFFFF,
                isBold = true,
                gradientText = true,
                gradientColors = listOf(0xFFFF6B6B, 0xFF4ECDC4),
                lineHeight = 1.3f
            )
        ),
        TextStylePreset(
            id = "clean",
            displayName = "Clean",
            settings = TextSettings(
                fontFamily = FontFamily.SANS_SERIF,
                fontSize = 42f,
                fontWeight = FontWeight.NORMAL,
                color = 0xFFF0F0F0,
                lineHeight = 1.5f,
                letterSpacing = 0.01f
            )
        ),
    )

    fun getById(id: String): TextStylePreset? = presets.find { it.id == id }
    fun getAll(): List<TextStylePreset> = presets
}
