package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorSwatchRow(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    colors: List<Pair<Long, String>> = defaultEditorColors(),
    showCustom: Boolean = true,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { (color, _) ->
            val isSelected = selectedColor == color
            val isLight = Color(color).luminance() > 0.55f
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(color))
                    .then(
                        if (isSelected) Modifier.border(
                            width = 3.dp,
                            color = if (isLight) Color(0xFF333333) else Color.White,
                            shape = RoundedCornerShape(10.dp)
                        ) else Modifier
                    )
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected && !isLight) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White)
                    )
                }
            }
        }

        if (showCustom) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                    .clickable { showPicker = true },
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    if (showPicker) {
        ColorPickerDialog(
            initialColor = selectedColor,
            onColorSelected = { onColorSelected(it) },
            onDismiss = { showPicker = false }
        )
    }
}

private fun Color.luminance(): Float {
    return 0.299f * red + 0.587f * green + 0.114f * blue
}

private fun defaultEditorColors() = listOf(
    0xFFFFFFFFL to "White",
    0xFF000000L to "Black",
    0xFF6C63FFL to "Purple",
    0xFFFF6584L to "Pink",
    0xFF3B82F6L to "Blue",
    0xFF27AE60L to "Green",
    0xFFF59E0BL to "Amber",
    0xFFE74C3CL to "Red",
    0xFFD4AF37L to "Gold",
    0xFF888888L to "Gray"
)
