package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quoteapp.model.TextSettings

@Composable
fun TextControls(
    style: TextSettings,
    onStyleChange: (TextSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Shadow", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)

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
            ColorSwatchRow(
                selectedColor = style.shadowColor,
                onColorSelected = { onStyleChange(style.copy(shadowColor = it)) },
                colors = shadowColors.map { it to "" }
            )
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

            val strokeColors = listOf(
                0xFF000000L to "Black", 0xFFFFFFFFL to "White", 0xFFFF0000L to "Red",
                0xFF0000FFL to "Blue", 0xFF00FF00L to "Green", 0xFFFFD700L to "Gold",
                0xFFFF69B4L to "Pink", 0xFF888888L to "Gray"
            )
            Text("Stroke Color", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ColorSwatchRow(
                selectedColor = style.strokeColor,
                onColorSelected = { onStyleChange(style.copy(strokeColor = it)) },
                colors = strokeColors
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Text Background", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Text Box", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Switch(
                checked = style.backgroundEnabled,
                onCheckedChange = { onStyleChange(style.copy(backgroundEnabled = it)) },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary, checkedTrackColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }

        if (style.backgroundEnabled) {
            StyleSlider(label = "Background Padding", value = style.backgroundPadding, valueRange = 0f..30f, step = 1f, onValueChange = { onStyleChange(style.copy(backgroundPadding = it)) })
            StyleSlider(label = "Background Corners", value = style.backgroundCorners, valueRange = 0f..30f, step = 1f, onValueChange = { onStyleChange(style.copy(backgroundCorners = it)) })

            val bgColors = listOf(
                0x66000000L to "Dark", 0x66FFFFFFL to "Light", 0x66FF0000L to "Red",
                0x660000FFL to "Blue", 0x66FFD700L to "Gold", 0x66FF69B4L to "Pink"
            )
            Text("Box Color", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            ColorSwatchRow(
                selectedColor = style.backgroundColor,
                onColorSelected = { onStyleChange(style.copy(backgroundColor = it)) },
                colors = bgColors
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Text Position", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)

        StyleSlider(label = "Position X", value = style.positionX, valueRange = 0f..1f, step = 0.01f, onValueChange = { onStyleChange(style.copy(positionX = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
        StyleSlider(label = "Position Y", value = style.positionY, valueRange = 0f..1f, step = 0.01f, onValueChange = { onStyleChange(style.copy(positionY = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
        StyleSlider(label = "Text Width", value = style.textWidth, valueRange = 0.3f..1f, step = 0.05f, onValueChange = { onStyleChange(style.copy(textWidth = it)) }, valueDisplay = { "${(it * 100).toInt()}%" })
    }
}
