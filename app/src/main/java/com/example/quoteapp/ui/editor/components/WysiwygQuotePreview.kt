package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quoteapp.model.EditorState
import com.example.quoteapp.model.QuoteBackground
import com.example.quoteapp.model.TextAlign as AppTextAlign
import com.example.quoteapp.model.TextTarget

@Composable
fun WysiwygQuotePreview(
    uiState: EditorState,
    onPositionChange: (TextTarget, Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val quoteStyle = uiState.quoteStyle
    val authorStyle = uiState.authorStyle
    val bgColor = when (val bg = uiState.background) {
        is QuoteBackground.SolidColor -> Color(bg.color)
        is QuoteBackground.Gradient -> Color(bg.colors.firstOrNull() ?: 0xFF1A1A2EL)
        is QuoteBackground.Image -> Color.LightGray
        is QuoteBackground.PngBackground -> Color(0xFF2A2A2A)
        is QuoteBackground.Programmatic -> Color(bg.baseColor)
    }

    val aspectRatioFloat = uiState.canvasSize.aspectRatio

    Box(
        modifier = modifier
            .aspectRatio(aspectRatioFloat)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.TopStart
    ) {
        if (uiState.background is QuoteBackground.Image) {
            Text(
                text = "[ Image Background ]",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (uiState.overlay.opacity > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(uiState.overlay.color).copy(alpha = uiState.overlay.opacity))
            )
        }

        DraggableTextLayer(
            text = uiState.quote,
            style = quoteStyle,
            isActive = uiState.activeTextTarget == TextTarget.QUOTE,
            onPositionChange = { x, y -> onPositionChange(TextTarget.QUOTE, x, y) }
        )

        if (uiState.author.isNotEmpty() || uiState.source.isNotEmpty()) {
            DraggableTextLayer(
                text = buildString {
                    if (uiState.author.isNotEmpty()) append("\u2014 ${uiState.author}")
                    if (uiState.source.isNotEmpty()) {
                        if (uiState.author.isNotEmpty()) append("\n")
                        append(uiState.source)
                    }
                },
                style = authorStyle,
                isActive = uiState.activeTextTarget == TextTarget.AUTHOR,
                onPositionChange = { x, y -> onPositionChange(TextTarget.AUTHOR, x, y) }
            )
        }
    }
}

@Composable
private fun DraggableTextLayer(
    text: String,
    style: com.example.quoteapp.model.TextSettings,
    isActive: Boolean,
    onPositionChange: (Float, Float) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val parentWidth = maxWidth
        val parentHeight = maxHeight

        val resolvedAlignment = when (style.alignment) {
            AppTextAlign.LEFT -> TextAlign.Left
            AppTextAlign.CENTER -> TextAlign.Center
            AppTextAlign.RIGHT -> TextAlign.Right
        }

        val fontWeight = if (style.isBold) androidx.compose.ui.text.font.FontWeight.Bold
        else when (style.fontWeight.weight) {
            100 -> androidx.compose.ui.text.font.FontWeight.Thin
            300 -> androidx.compose.ui.text.font.FontWeight.Light
            400 -> androidx.compose.ui.text.font.FontWeight.Normal
            500 -> androidx.compose.ui.text.font.FontWeight.Medium
            700 -> androidx.compose.ui.text.font.FontWeight.Bold
            900 -> androidx.compose.ui.text.font.FontWeight.Black
            else -> androidx.compose.ui.text.font.FontWeight.Normal
        }

        val textStyle = TextStyle(
            fontSize = style.fontSize.sp,
            fontWeight = fontWeight,
            fontStyle = if (style.isItalic) FontStyle.Italic else FontStyle.Normal,
            color = Color(style.color).copy(alpha = style.opacity),
            letterSpacing = style.letterSpacing.sp,
            lineHeight = (style.fontSize * style.lineHeight).sp,
            textAlign = resolvedAlignment,
            textDecoration = if (style.strokeEnabled && style.strokeWidth > 0f) {
                TextDecoration.combine(listOf(TextDecoration.None))
            } else null,
            shadow = if (style.shadowEnabled) {
                Shadow(
                    color = Color(style.shadowColor),
                    offset = Offset(style.shadowDx, style.shadowDy),
                    blurRadius = style.shadowRadius
                )
            } else null,
            drawStyle = if (style.strokeEnabled) {
                Stroke(width = style.strokeWidth)
            } else null
        )

        if (style.backgroundEnabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = style.rotation
                        translationX = (style.positionX * size.width) - (size.width / 2f)
                        translationY = (style.positionY * size.height) - (size.height / 2f)
                    }
                    .let { mod ->
                        if (isActive) {
                            mod.pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    val w = size.width.toFloat()
                                    val h = size.height.toFloat()
                                    onPositionChange(
                                        (change.position.x / w).coerceIn(0f, 1f),
                                        (change.position.y / h).coerceIn(0f, 1f)
                                    )
                                }
                            }
                        } else mod
                    },
                contentAlignment = when (style.alignment) {
                    AppTextAlign.LEFT -> Alignment.TopStart
                    AppTextAlign.CENTER -> Alignment.Center
                    AppTextAlign.RIGHT -> Alignment.TopEnd
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = style.backgroundPadding.dp,
                            vertical = style.backgroundPadding.dp * 0.5f
                        )
                        .background(
                            Color(style.backgroundColor),
                            RoundedCornerShape(style.backgroundCorners.dp)
                        )
                        .padding(
                            horizontal = style.backgroundPadding.dp,
                            vertical = style.backgroundPadding.dp * 0.5f
                        ),
                    horizontalAlignment = when (style.alignment) {
                        AppTextAlign.LEFT -> Alignment.Start
                        AppTextAlign.CENTER -> Alignment.CenterHorizontally
                        AppTextAlign.RIGHT -> Alignment.End
                    }
                ) {
                    if (text.isNotEmpty()) {
                        Text(text = text, style = textStyle, modifier = Modifier.padding(horizontal = 4.dp))
                    } else {
                        Text(
                            text = "Your quote here...",
                            style = textStyle,
                            color = Color(style.color).copy(alpha = 0.3f),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        } else {
            Text(
                text = text.ifEmpty { "Your quote here..." },
                style = textStyle,
                color = if (text.isEmpty()) Color(style.color).copy(alpha = 0.3f)
                else Color(style.color).copy(alpha = style.opacity),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationZ = style.rotation
                        translationX = (style.positionX * size.width) - (size.width / 2f)
                        translationY = (style.positionY * size.height) - (size.height / 2f)
                    }
                    .let { mod ->
                        if (isActive) {
                            mod.pointerInput(Unit) {
                                detectDragGestures { change, _ ->
                                    change.consume()
                                    val w = size.width.toFloat()
                                    val h = size.height.toFloat()
                                    onPositionChange(
                                        (change.position.x / w).coerceIn(0f, 1f),
                                        (change.position.y / h).coerceIn(0f, 1f)
                                    )
                                }
                            }
                        } else mod
                    }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}
