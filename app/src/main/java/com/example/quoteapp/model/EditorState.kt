package com.example.quoteapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditorState(
    val canvasSize: CanvasSize = CanvasSize(1080, 1080),
    val aspectRatio: AspectRatio = AspectRatios.default(),
    val quote: String = "",
    val author: String = "",
    val source: String = "",
    val template: QuoteTemplate? = null,
    val background: QuoteBackground = QuoteBackground.SolidColor(0xFF1A1A2EL),
    val quoteStyle: TextSettings = TextSettings(),
    val authorStyle: TextSettings = TextSettings(fontSize = 24f, positionY = 0.65f),
    val overlay: OverlaySettings = OverlaySettings(),
    val decorations: List<Decoration> = emptyList(),
    val exportSettings: ExportSettings = ExportSettings(),
    val activeTab: EditorTab = EditorTab.TEMPLATE,
    val activeTextTarget: TextTarget = TextTarget.QUOTE
) : Parcelable

enum class EditorTab(val displayName: String) {
    TEMPLATE("Template"),
    TEXT("Text"),
    BACKGROUND("Background"),
    STYLE("Style"),
    RATIO("Ratio"),
    LAYOUT("Layout")
}

enum class TextTarget(val displayName: String) {
    QUOTE("Quote"),
    AUTHOR("Author")
}
