package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quoteapp.model.TextSettings

@Composable
fun TextControls(
    style: TextSettings,
    onStyleChange: (TextSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Shadow",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Shadow", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Switch(
                checked = style.shadowEnabled,
                onCheckedChange = { onStyleChange(style.copy(shadowEnabled = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary, checkedTrackColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }

        if (style.shadowEnabled) {
            StyleSlider(label = "Shadow Radius", value = style.shadowRadius, valueRange = 0f..30f, step = 1f, onValueChange = { onStyleChange(style.copy(shadowRadius = it)) })
            StyleSlider(label = "Shadow Offset X", value = style.shadowDx, valueRange = -10f..10f, step = 0.5f, onValueChange = { onStyleChange(style.copy(shadowDx = it)) }, valueDisplay = { "${it.toInt()}" })
            StyleSlider(label = "Shadow Offset Y", value = style.shadowDy, valueRange = -10f..10f, step = 0.5f, onValueChange = { onStyleChange(style.copy(shadowDy = it)) }, valueDisplay = { "${it.toInt()}" })

            val shadowColors = listOf(0x66000000L, 0x66FFFFFFL, 0x66FF0000L, 0x660000FFL, 0x66FFD700L, 0x66FF69B4L)
            Text("Shadow Color", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                shadowColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(color))
                            .then(if (style.shadowColor == color) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp)) else Modifier)
                            .clickable { onStyleChange(style.copy(shadowColor = color)) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Stroke / Outline", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Stroke", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Switch(
                checked = style.strokeEnabled,
                onCheckedChange = { onStyleChange(style.copy(strokeEnabled = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary, checkedTrackColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }

        if (style.strokeEnabled) {
            StyleSlider(label = "Stroke Width", value = style.strokeWidth, valueRange = 0.5f..10f, step = 0.5f, onValueChange = { onStyleChange(style.copy(strokeWidth = it)) })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Text Position", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)

        StyleSlider(label = "Position X", value = style.positionX, valueRange = 0f..1f, step = 0.01f, onValueChange = { onStyleChange(style.copy(positionX = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
        StyleSlider(label = "Position Y", value = style.positionY, valueRange = 0f..1f, step = 0.01f, onValueChange = { onStyleChange(style.copy(positionY = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
        StyleSlider(label = "Text Width", value = style.textWidth, valueRange = 0.3f..1f, step = 0.05f, onValueChange = { onStyleChange(style.copy(textWidth = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
    }
}

@Composable
private fun StyleSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    onValueChange: (Float) -> Unit,
    valueDisplay: (Float) -> String = { String.format("%.2f", it) }
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(valueDisplay(value), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) / step).toInt() - 1,
            colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary, activeTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}
