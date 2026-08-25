package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class QuoteBackground : Parcelable {
    abstract val id: String

    @Parcelize
    data class SolidColor(
        val color: Long,
        override val id: String = "solid_$color"
    ) : QuoteBackground()

    @Parcelize
    data class Gradient(
        val colors: List<Long>,
        val angle: Float = 0f,
        val type: GradientType = GradientType.LINEAR,
        val colorStops: List<GradientColorStop>? = null,
        override val id: String = "gradient_${colors.hashCode()}"
    ) : QuoteBackground()

    @Parcelize
    data class Image(
        val uri: String,
        val cropX: Float = 0.5f,
        val cropY: Float = 0.5f,
        val scale: Float = 1f,
        val rotation: Float = 0f,
        val blur: Float = 0f,
        val brightness: Float = 0f,
        val contrast: Float = 0f,
        val overlayColor: Long = 0x00000000L,
        val overlayOpacity: Float = 0f,
        val fitMode: ImageFitMode = ImageFitMode.COVER,
        override val id: String = "image_${uri.hashCode()}"
    ) : QuoteBackground()

    @Parcelize
    data class PngBackground(
        val assetPath: String,
        val blur: Float = 0f,
        val brightness: Float = 0f,
        val overlayColor: Long = 0x00000000L,
        val overlayOpacity: Float = 0f,
        override val id: String = "png_${assetPath.hashCode()}"
    ) : QuoteBackground()

    @Parcelize
    data class Programmatic(
        val pattern: PatternType,
        val baseColor: Long = 0xFF1A1A2EL,
        val accentColor: Long = 0xFF16213EL,
        override val id: String = "pattern_${pattern.name}"
    ) : QuoteBackground()
}

enum class GradientType { LINEAR, RADIAL, SWEEP }
enum class ImageFitMode { COVER, CONTAIN, FILL, CENTER }
enum class PatternType {
    NOISE, PAPER, GEOMETRIC, SOFT_GRADIENT,
    DARK_TEXTURE, ABSTRACT_GRADIENT, MINIMAL
}
