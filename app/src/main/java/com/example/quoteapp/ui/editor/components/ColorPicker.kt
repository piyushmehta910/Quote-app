package com.example.quoteapp.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
    showCustomInput: Boolean = false,
    onCustomColorInput: ((String) -> Unit)? = null
) {
    val presetColors = listOf(
        0xFFFFFFFFL to "White",
        0xFF000000L to "Black",
        0xFFE74C3CL to "Red",
        0xFFE91E63L to "Pink",
        0xFF9C27B0L to "Purple",
        0xFF673AB7L to "Deep Purple",
        0xFF3F51B5L to "Indigo",
        0xFF2196F3L to "Blue",
        0xFF03A9F4L to "Light Blue",
        0xFF00BCD4L to "Cyan",
        0xFF009688L to "Teal",
        0xFF4CAF50L to "Green",
        0xFF8BC34AL to "Light Green",
        0xFFCDDC39L to "Lime",
        0xFFFFEB3BL to "Yellow",
        0xFFFFC107L to "Amber",
        0xFFFF9800L to "Orange",
        0xFFFF5722L to "Deep Orange",
        0xFF795548L to "Brown",
        0xFF607D8BL to "Blue Grey",
        0xFFD4AF37L to "Gold",
        0xFFC0C0C0L to "Silver",
        0xFFFF6B6BL to "Coral",
        0xFF4ECDC4L to "Turquoise",
        0xFF45B7D1L to "Sky Blue",
        0xFF96E6A1L to "Mint",
        0xFFDDA0DDL to "Plum",
        0xFFF093FBL to "Orchid",
        0xFFF5576CL to "Rose",
        0xFF1A1A2EL to "Navy",
        0xFF2C3E50L to "Midnight",
        0xFF0F3460L to "Deep Blue"
    )

    Column(modifier = modifier) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            presetColors.forEach { (color, name) ->
                val isSelected = selectedColor == color
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(color))
                        .then(
                            if (isSelected) Modifier.padding(2.dp)
                            else Modifier
                        )
                        .clickable { onColorSelected(color) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }
}
