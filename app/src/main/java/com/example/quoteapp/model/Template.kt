package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuoteTemplate(
    val id: String,
    val name: String,
    val category: TemplateCategory,
    val background: QuoteBackground,
    val overlay: OverlaySettings = OverlaySettings(),
    val quoteStyle: TextSettings = TextSettings(),
    val authorStyle: TextSettings = TextSettings(fontSize = 24f, positionY = 0.65f),
    val decorations: List<Decoration> = emptyList(),
    val normalizedLayout: NormalizedLayout = NormalizedLayout(),
    val thumbnailColor: Long = 0xFF1A1A2EL,
    val isPremium: Boolean = false
) : Parcelable

@Parcelize
data class OverlaySettings(
    val color: Long = 0x00000000L,
    val opacity: Float = 0f
) : Parcelable

@Parcelize
data class NormalizedLayout(
    val quoteBounds: NormalizedRect = NormalizedRect(0.1f, 0.3f, 0.8f, 0.3f),
    val authorBounds: NormalizedRect = NormalizedRect(0.1f, 0.65f, 0.8f, 0.1f)
) : Parcelable

@Parcelize
data class NormalizedRect(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
) : Parcelable

@Parcelize
sealed class Decoration : Parcelable {
    @Parcelize
    data class Line(
        val color: Long = 0xFFFFFFFFL,
        val strokeWidth: Float = 2f,
        val y: Float = 0.6f,
        val startX: Float = 0.3f,
        val endX: Float = 0.7f
    ) : Decoration()

    @Parcelize
    data class QuoteMarks(
        val color: Long = 0xFFFFFFFFL,
        val size: Float = 64f
    ) : Decoration()

    @Parcelize
    data class Circle(
        val color: Long = 0x33FFFFFFL,
        val radius: Float = 0.4f,
        val centerX: Float = 0.5f,
        val centerY: Float = 0.5f,
        val strokeWidth: Float = 2f
    ) : Decoration()

    @Parcelize
    data class Border(
        val color: Long = 0xFFFFFFFFL,
        val strokeWidth: Float = 2f,
        val inset: Float = 0.05f
    ) : Decoration()
}

enum class TemplateCategory(val displayName: String) {
    MOTIVATION("Motivation"),
    SUCCESS("Success"),
    LIFE("Life"),
    LOVE("Love"),
    FRIENDSHIP("Friendship"),
    STUDY("Study"),
    BUSINESS("Business"),
    FITNESS("Fitness"),
    ATTITUDE("Attitude"),
    SPIRITUAL("Spiritual"),
    MINIMAL("Minimal"),
    DARK("Dark"),
    AESTHETIC("Aesthetic"),
    CINEMATIC("Cinematic"),
    INSPIRATIONAL("Inspirational")
}
