package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPickerDialog(
    initialColor: Long,
    onColorSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var currentColor by remember { mutableStateOf(Color(initialColor)) }
    var hexInput by remember { mutableStateOf(colorToHex(initialColor)) }
    var hue by remember { mutableStateOf(colorToHsl(initialColor).first) }
    var saturation by remember { mutableStateOf(colorToHsl(initialColor).second) }
    var lightness by remember { mutableStateOf(colorToHsl(initialColor).third) }

    fun syncFromHsl() {
        currentColor = Color.hsl(hue, saturation, lightness)
        hexInput = colorToHex(currentColor.toArgb().toLong())
    }

    fun syncFromHex(hex: String) {
        val parsed = try {
            val cleaned = hex.removePrefix("#")
            val argb = cleaned.toLong(16)
            if (cleaned.length == 6) (argb or 0xFF000000L) else argb
        } catch (_: Exception) { null }
        if (parsed != null) {
            val c = Color(parsed.toInt())
            currentColor = c
            val hsl = colorToHsl(parsed)
            hue = hsl.first
            saturation = hsl.second
            lightness = hsl.third
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick a Color") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(currentColor)
                            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    )
                    OutlinedTextField(
                        value = hexInput,
                        onValueChange = { newHex ->
                            hexInput = newHex
                            syncFromHex(newHex)
                        },
                        label = { Text("Hex") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                Text("Hue", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Slider(
                    value = hue,
                    onValueChange = { hue = it; syncFromHsl() },
                    valueRange = 0f..360f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.hsl(hue, 1f, 0.5f),
                        activeTrackColor = Color.hsl(hue, 1f, 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Saturation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Slider(
                    value = saturation,
                    onValueChange = { saturation = it; syncFromHsl() },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.hsl(hue, saturation, lightness),
                        activeTrackColor = Color.hsl(hue, 1f, 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Lightness", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Slider(
                    value = lightness,
                    onValueChange = { lightness = it; syncFromHsl() },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.hsl(hue, saturation, lightness),
                        activeTrackColor = Color.hsl(hue, saturation, 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("RGB", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val r = currentColor.red
                val g = currentColor.green
                val b = currentColor.blue

                listOf(
                    Triple("R", r) { v: Float -> currentColor.copy(red = v) },
                    Triple("G", g) { v: Float -> currentColor.copy(green = v) },
                    Triple("B", b) { v: Float -> currentColor.copy(blue = v) }
                ).forEach { (label, value, onChannelChange) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(20.dp))
                        Slider(
                            value = value,
                            onValueChange = { newV ->
                                currentColor = onChannelChange(newV)
                                hexInput = colorToHex(currentColor.toArgb().toLong())
                                val hsl = colorToHsl(currentColor.toArgb().toLong())
                                hue = hsl.first; saturation = hsl.second; lightness = hsl.third
                            },
                            valueRange = 0f..1f,
                            modifier = Modifier.weight(1f)
                        )
                        Text("${(value * 255).toInt()}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.width(36.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text("Quick Pick", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickPickColors.forEach { preset ->
                        val isSelected = currentColor == preset
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(preset)
                                .then(
                                    if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
                                    else Modifier
                                )
                                .clickable {
                                    currentColor = preset
                                    hexInput = colorToHex(preset.toArgb().toLong())
                                    val hsl = colorToHsl(preset.toArgb().toLong())
                                    hue = hsl.first; saturation = hsl.second; lightness = hsl.third
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onColorSelected(currentColor.toArgb().toLong())
                onDismiss()
            }) { Text("Select") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun colorToHsl(colorLong: Long): Triple<Float, Float, Float> {
    val c = Color(colorLong.toInt())
    val r = c.red; val g = c.green; val b = c.blue
    val max = maxOf(r, g, b); val min = minOf(r, g, b)
    val l = (max + min) / 2f
    if (max == min) return Triple(0f, 0f, l)
    val d = max - min
    val s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)
    val h = when (max) {
        r -> ((g - b) / d + (if (g < b) 6f else 0f)) / 6f
        g -> ((b - r) / d + 2f) / 6f
        else -> ((r - g) / d + 4f) / 6f
    }
    return Triple(h * 360f, s, l)
}

private fun colorToHex(colorLong: Long): String {
    val argb = colorLong.toInt()
    return String.format("%06X", argb and 0xFFFFFF)
}

private val quickPickColors = listOf(
    Color.White, Color.Black, Color(0xFFE74C3C), Color(0xFFE91E63),
    Color(0xFF9C27B0), Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
    Color(0xFF03A9F4), Color(0xFF00BCD4), Color(0xFF009688), Color(0xFF4CAF50),
    Color(0xFF8BC34A), Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
    Color(0xFFFF5722), Color(0xFF795548), Color(0xFF607D8B), Color(0xFFD4AF37),
    Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFF45B7D1), Color(0xFF1A1A2E)
)
