package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    val id: String,
    val name: String,
    val quote: String,
    val author: String,
    val source: String = "",
    val templateId: String? = null,
    val canvasSize: CanvasSize = CanvasSize(1080, 1080),
    val background: QuoteBackground = QuoteBackground.SolidColor(0xFF1A1A2EL),
    val quoteStyle: TextSettings = TextSettings(),
    val authorStyle: TextSettings = TextSettings(fontSize = 24f, positionY = 0.65f),
    val overlay: OverlaySettings = OverlaySettings(),
    val exportSettings: ExportSettings = ExportSettings(),
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class ExportSettings(
    val format: ExportFormat = ExportFormat.PNG,
    val quality: ExportQuality = ExportQuality.HIGH,
    val customWidth: Int = 0,
    val customHeight: Int = 0,
    val useCustomSize: Boolean = false
) : Parcelable

enum class ExportFormat(val displayName: String, val mimeType: String, val extension: String) {
    PNG("PNG", "image/png", "png"),
    JPEG("JPEG", "image/jpeg", "jpg"),
    WEBP("WebP", "image/webp", "webp")
}

enum class ExportQuality(val displayName: String, val value: Int) {
    COMPRESSED("Compressed", 60),
    STANDARD("Standard", 80),
    HIGH("High", 95)
}
