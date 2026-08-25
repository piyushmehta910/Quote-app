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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quoteapp.model.GradientColorStop
import com.example.quoteapp.model.GradientType
import com.example.quoteapp.model.QuoteBackground

@Composable
fun GradientEditor(
    gradient: QuoteBackground.Gradient,
    onGradientChange: (QuoteBackground.Gradient) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStopIndex by remember { mutableStateOf(0) }

    val colorStops = gradient.colorStops ?: gradient.colors.mapIndexed { index, color ->
        GradientColorStop(
            color = color,
            position = if (gradient.colors.size == 1) 0f
            else index.toFloat() / (gradient.colors.size - 1)
        )
    }

    Column(modifier = modifier) {
        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        val brush = when (gradient.type) {
            GradientType.LINEAR -> Brush.linearGradient(
                colors = colorStops.map { Color(it.color) }
            )
            GradientType.RADIAL -> Brush.radialGradient(
                colors = colorStops.map { Color(it.color) }
            )
            GradientType.SWEEP -> Brush.sweepGradient(
                colors = colorStops.map { Color(it.color) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Color Stops",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(colorStops) { index, stop ->
                val isSelected = index == selectedStopIndex
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(stop.color))
                        .then(
                            if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                            else Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                        )
                        .clickable {
                            selectedStopIndex = index
                        }
                )
            }

            if (colorStops.size < 8) {
                item {
                    IconButton(
                        onClick = {
                            val newStop = GradientColorStop(
                                color = 0xFF888888L,
                                position = 0.5f
                            )
                            val newStops = (colorStops + newStop).sortedBy { it.position }
                            onGradientChange(gradient.copy(colorStops = newStops))
                            selectedStopIndex = newStops.indexOf(newStop).coerceAtLeast(0)
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add color stop",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        if (colorStops.size > 2 && selectedStopIndex in colorStops.indices) {
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(
                onClick = {
                    val newStops = colorStops.toMutableList()
                    newStops.removeAt(selectedStopIndex)
                    selectedStopIndex = (selectedStopIndex - 1).coerceAtLeast(0)
                    onGradientChange(gradient.copy(colorStops = newStops))
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remove color stop",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        if (selectedStopIndex in colorStops.indices) {
            Spacer(modifier = Modifier.height(12.dp))

            val presetColors = listOf(
                0xFFFFFFFFL, 0xFF000000L, 0xFFE74C3CL, 0xFFE91E63L,
                0xFF9C27B0L, 0xFF673AB7L, 0xFF3F51B5L, 0xFF2196F3L,
                0xFF00BCD4L, 0xFF009688L, 0xFF4CAF50L, 0xFF8BC34AL,
                0xFFFFEB3BL, 0xFFFF9800L, 0xFF795548L, 0xFF607D8BL,
                0xFFD4AF37L, 0xFFFF6B6BL, 0xFF4ECDC4L, 0xFF45B7D1L,
                0xFF96E6A1L, 0xFFDDA0DDL, 0xFFF093FBL, 0xFFF5576CL
            )

            Text(
                text = "Color",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(presetColors) { color ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(color))
                            .clickable {
                                val newStops = colorStops.toMutableList()
                                newStops[selectedStopIndex] = newStops[selectedStopIndex].copy(color = color)
                                onGradientChange(gradient.copy(colorStops = newStops))
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Type",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GradientType.entries.forEach { type ->
                androidx.compose.material3.FilterChip(
                    selected = gradient.type == type,
                    onClick = { onGradientChange(gradient.copy(type = type)) },
                    label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Angle: ${gradient.angle.toInt()}°",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Slider(
            value = gradient.angle,
            onValueChange = { onGradientChange(gradient.copy(angle = it)) },
            valueRange = 0f..360f,
            steps = 35,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
