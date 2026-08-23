package com.example.quoteapp.model

import android.graphics.Typeface
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextSettings(
    val fontFamily: FontFamily = FontFamily.DEFAULT,
    val fontSize: Float = 48f,
    val fontWeight: FontWeight = FontWeight.NORMAL,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val color: Long = 0xFFFFFFFFL,
    val opacity: Float = 1f,
    val alignment: TextAlign = TextAlign.CENTER,
    val lineHeight: Float = 1.4f,
    val letterSpacing: Float = 0f,
    val textWidth: Float = 0.85f,
    val positionX: Float = 0.5f,
    val positionY: Float = 0.5f,
    val rotation: Float = 0f,
    val shadowEnabled: Boolean = false,
    val shadowColor: Long = 0x66000000L,
    val shadowRadius: Float = 4f,
    val shadowDx: Float = 2f,
    val shadowDy: Float = 2f,
    val strokeEnabled: Boolean = false,
    val strokeColor: Long = 0xFF000000L,
    val strokeWidth: Float = 1f,
    val highlightEnabled: Boolean = false,
    val highlightColor: Long = 0x44FFFF00L,
    val backgroundEnabled: Boolean = false,
    val backgroundColor: Long = 0x33000000L,
    val backgroundPadding: Float = 8f,
    val backgroundCorners: Float = 8f,
    val gradientText: Boolean = false,
    val gradientColors: List<Long> = listOf(0xFFFF6B6BL, 0xFF4ECDC4L),
    val autoFit: Boolean = true,
    val minFontSize: Float = 12f,
    val maxFontSize: Float = 120f,
    val maxLines: Int = 0
) : Parcelable

@Parcelize
enum class FontFamily(val displayName: String) : Parcelable {
    DEFAULT("Default"),
    SERIF("Serif"),
    SANS_SERIF("Sans Serif"),
    MONOSPACE("Monospace");

    fun typeface(style: Int = Typeface.NORMAL): Typeface {
        return when (this) {
            DEFAULT -> Typeface.create("sans-serif", style)
            SERIF -> Typeface.create("serif", style)
            SANS_SERIF -> Typeface.create("sans-serif", style)
            MONOSPACE -> Typeface.create("monospace", style)
        }
    }
}

@Parcelize
enum class FontWeight(val displayName: String, val weight: Int) : Parcelable {
    THIN("Thin", 100),
    LIGHT("Light", 300),
    NORMAL("Normal", 400),
    MEDIUM("Medium", 500),
    BOLD("Bold", 700),
    BLACK("Black", 900)
}

@Parcelize
enum class TextAlign(val displayName: String) : Parcelable {
    LEFT("Left"),
    CENTER("Center"),
    RIGHT("Right")
}

@Parcelize
data class TextStylePreset(
    val id: String,
    val displayName: String,
    val settings: TextSettings,
    val isPremium: Boolean = false
) : Parcelable
