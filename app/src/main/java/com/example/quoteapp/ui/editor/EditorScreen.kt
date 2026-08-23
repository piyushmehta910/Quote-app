package com.example.quoteapp.ui.editor

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.data.TextStylePresets
import com.example.quoteapp.model.AspectRatios
import com.example.quoteapp.model.EditorTab
import com.example.quoteapp.model.EditorState
import com.example.quoteapp.model.ExportFormat
import com.example.quoteapp.model.ExportQuality
import com.example.quoteapp.model.FontFamily
import com.example.quoteapp.model.FontWeight
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight
import com.example.quoteapp.model.QuoteBackground
import com.example.quoteapp.model.TextAlign
import com.example.quoteapp.model.TextSettings
import com.example.quoteapp.model.TextTarget
import com.example.quoteapp.renderer.ExportEngine
import kotlinx.coroutines.launch

private fun FontFamily.toComposeFamily(): ComposeFontFamily = when (this) {
    FontFamily.DEFAULT -> ComposeFontFamily.Default
    FontFamily.SERIF -> ComposeFontFamily.Serif
    FontFamily.SANS_SERIF -> ComposeFontFamily.SansSerif
    FontFamily.MONOSPACE -> ComposeFontFamily.Monospace
}

private fun FontWeight.toComposeWeight(): ComposeFontWeight = ComposeFontWeight(weight)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    projectId: String? = null,
    templateId: String? = null,
    quoteText: String? = null,
    onNavigateBack: () -> Unit = {},
    viewModel: EditorViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.state.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val canRedo by viewModel.canRedo.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()

    var selectedTab by remember { mutableIntStateOf(1) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            scope.launch { performExport(context, viewModel, snackbarHostState, isShare = false) }
        } else {
            scope.launch { snackbarHostState.showSnackbar("Storage permission required to export") }
        }
    }

    LaunchedEffect(projectId, templateId, quoteText) {
        when {
            projectId != null -> viewModel.loadProject(projectId)
            templateId != null -> viewModel.loadTemplate(templateId)
            quoteText != null -> viewModel.updateQuote(quoteText)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quote Editor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            viewModel.saveProject()
                            snackbarHostState.showSnackbar("Project saved")
                        }
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Save")
                    }
                    IconButton(onClick = {
                        scope.launch {
                            performExport(context, viewModel, snackbarHostState, isShare = true)
                        }
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomActionBar(
                canUndo = canUndo,
                canRedo = canRedo,
                onUndo = { viewModel.undo() },
                onRedo = { viewModel.redo() },
                onExport = {
                    permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                },
                onShare = {
                    scope.launch {
                        performExport(context, viewModel, snackbarHostState, isShare = true)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isExporting) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            QuotePreview(
                uiState = uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier,
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0; viewModel.setActiveTab(EditorTab.TEXT) },
                    text = { Text("Text", style = MaterialTheme.typography.labelMedium) },
                    icon = { Icon(Icons.Filled.TextFields, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; viewModel.setActiveTab(EditorTab.STYLE) },
                    text = { Text("Style", style = MaterialTheme.typography.labelMedium) },
                    icon = { Icon(Icons.Filled.TextFormat, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2; viewModel.setActiveTab(EditorTab.BACKGROUND) },
                    text = { Text("Canvas", style = MaterialTheme.typography.labelMedium) },
                    icon = { Icon(Icons.Filled.Tune, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> TextTab(uiState = uiState, viewModel = viewModel)
                    1 -> StyleTab(uiState = uiState, viewModel = viewModel)
                    2 -> SettingsTab(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun QuotePreview(
    uiState: EditorState,
    modifier: Modifier = Modifier
) {
    val quoteStyle = uiState.quoteStyle
    val authorStyle = uiState.authorStyle
    val bgColor = when (val bg = uiState.background) {
        is QuoteBackground.SolidColor -> Color(bg.color)
        is QuoteBackground.Gradient -> Color(bg.colors.firstOrNull() ?: 0xFF1A1A2EL)
        is QuoteBackground.Image -> Color.LightGray
        is QuoteBackground.Programmatic -> Color(bg.baseColor)
    }

    val aspectRatioFloat = uiState.canvasSize.aspectRatio

    Box(
        modifier = modifier
            .aspectRatio(aspectRatioFloat)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.background is QuoteBackground.Image) {
            Text(text = "[ Image Background ]", color = Color.Gray, fontSize = 14.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = when (quoteStyle.alignment) {
                TextAlign.LEFT -> Alignment.Start
                TextAlign.RIGHT -> Alignment.End
                TextAlign.CENTER -> Alignment.CenterHorizontally
            },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.quote.ifEmpty { "Your quote here..." },
                color = if (uiState.quote.isEmpty()) Color(quoteStyle.color).copy(alpha = 0.5f) else Color(quoteStyle.color),
                fontSize = quoteStyle.fontSize.sp,
                fontWeight = if (quoteStyle.isBold) ComposeFontWeight.Bold else quoteStyle.fontWeight.toComposeWeight(),
                fontStyle = if (quoteStyle.isItalic) FontStyle.Italic else FontStyle.Normal,
                letterSpacing = quoteStyle.letterSpacing.sp,
                lineHeight = (quoteStyle.fontSize * quoteStyle.lineHeight).sp,
                textAlign = when (quoteStyle.alignment) {
                    TextAlign.LEFT -> androidx.compose.ui.text.style.TextAlign.Left
                    TextAlign.CENTER -> androidx.compose.ui.text.style.TextAlign.Center
                    TextAlign.RIGHT -> androidx.compose.ui.text.style.TextAlign.Right
                },
                fontFamily = quoteStyle.fontFamily.toComposeFamily(),
                modifier = Modifier.rotate(quoteStyle.rotation)
            )

            if (uiState.author.isNotEmpty() || uiState.source.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = buildString {
                        if (uiState.author.isNotEmpty()) append("\u2014 ${uiState.author}")
                        if (uiState.source.isNotEmpty()) {
                            if (uiState.author.isNotEmpty()) append("\n")
                            append(uiState.source)
                        }
                    },
                    color = Color(authorStyle.color),
                    fontSize = authorStyle.fontSize.sp,
                    fontWeight = ComposeFontWeight.Normal,
                    fontStyle = if (authorStyle.isItalic) FontStyle.Italic else FontStyle.Normal,
                    letterSpacing = 0.5.sp,
                    textAlign = when (authorStyle.alignment) {
                        TextAlign.LEFT -> androidx.compose.ui.text.style.TextAlign.Left
                        TextAlign.CENTER -> androidx.compose.ui.text.style.TextAlign.Center
                        TextAlign.RIGHT -> androidx.compose.ui.text.style.TextAlign.Right
                    },
                    fontFamily = authorStyle.fontFamily.toComposeFamily(),
                    modifier = Modifier.rotate(authorStyle.rotation)
                )
            }
        }
    }
}

@Composable
private fun TextTab(
    uiState: EditorState,
    viewModel: EditorViewModel
) {
    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = uiState.activeTextTarget == TextTarget.QUOTE,
                onClick = { viewModel.setActiveTextTarget(TextTarget.QUOTE) },
                label = { Text("Quote") },
                leadingIcon = {
                    if (uiState.activeTextTarget == TextTarget.QUOTE) {
                        Icon(Icons.Filled.TextFields, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            FilterChip(
                selected = uiState.activeTextTarget == TextTarget.AUTHOR,
                onClick = { viewModel.setActiveTextTarget(TextTarget.AUTHOR) },
                label = { Text("Author") },
                leadingIcon = {
                    if (uiState.activeTextTarget == TextTarget.AUTHOR) {
                        Icon(Icons.Filled.TextFields, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }

        TextField(
            value = uiState.quote,
            onValueChange = { viewModel.updateQuote(it) },
            label = { Text("Quote Text") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            colors = fieldColors
        )

        TextField(
            value = uiState.author,
            onValueChange = { viewModel.updateAuthor(it) },
            label = { Text("Author Name") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = fieldColors
        )

        TextField(
            value = uiState.source,
            onValueChange = { viewModel.updateSource(it) },
            label = { Text("Source / Attribution (optional)") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            colors = fieldColors
        )
    }
}

@Composable
private fun StyleTab(
    uiState: EditorState,
    viewModel: EditorViewModel
) {
    val currentStyle = if (uiState.activeTextTarget == TextTarget.QUOTE)
        uiState.quoteStyle else uiState.authorStyle

    val updateStyle: (TextSettings) -> Unit = { newStyle ->
        viewModel.updateCurrentTextStyle(newStyle)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Style Presets",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(TextStylePresets.getAll()) { preset ->
                OutlinedCard(
                    modifier = Modifier
                        .width(100.dp)
                        .clickable { viewModel.applyTextStylePreset(preset) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"Aa\"",
                            fontSize = (preset.settings.fontSize / 4).sp,
                            fontWeight = if (preset.settings.isBold) ComposeFontWeight.Bold else ComposeFontWeight.Normal,
                            fontStyle = if (preset.settings.isItalic) FontStyle.Italic else FontStyle.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = preset.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Text(
            text = if (uiState.activeTextTarget == TextTarget.QUOTE) "Quote Styling" else "Author Styling",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        StyleSlider(
            label = "Font Size",
            value = currentStyle.fontSize,
            valueRange = 12f..120f,
            step = 1f,
            onValueChange = { updateStyle(currentStyle.copy(fontSize = it)) }
        )

        StyleSlider(
            label = "Opacity",
            value = currentStyle.opacity,
            valueRange = 0f..1f,
            step = 0.05f,
            onValueChange = { updateStyle(currentStyle.copy(opacity = it)) },
            valueDisplay = { "${(it * 100).toInt()}%" }
        )

        StyleSlider(
            label = "Letter Spacing",
            value = currentStyle.letterSpacing,
            valueRange = -0.05f..0.2f,
            step = 0.01f,
            onValueChange = { updateStyle(currentStyle.copy(letterSpacing = it)) }
        )

        StyleSlider(
            label = "Line Height",
            value = currentStyle.lineHeight,
            valueRange = 0.8f..2.5f,
            step = 0.1f,
            onValueChange = { updateStyle(currentStyle.copy(lineHeight = it)) }
        )

        StyleSlider(
            label = "Rotation",
            value = currentStyle.rotation,
            valueRange = -180f..180f,
            step = 1f,
            onValueChange = { updateStyle(currentStyle.copy(rotation = it)) },
            valueDisplay = { "${it.toInt()}\u00B0" }
        )

        Text(
            text = "Toggles",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ToggleSwitch(
                label = "Bold",
                checked = currentStyle.isBold,
                onToggle = { updateStyle(currentStyle.copy(isBold = !currentStyle.isBold)) },
                modifier = Modifier.weight(1f)
            )
            ToggleSwitch(
                label = "Italic",
                checked = currentStyle.isItalic,
                onToggle = { updateStyle(currentStyle.copy(isItalic = !currentStyle.isItalic)) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ToggleSwitch(
                label = "Shadow",
                checked = currentStyle.shadowEnabled,
                onToggle = { updateStyle(currentStyle.copy(shadowEnabled = !currentStyle.shadowEnabled)) },
                modifier = Modifier.weight(1f)
            )
            ToggleSwitch(
                label = "Auto Fit",
                checked = currentStyle.autoFit,
                onToggle = { updateStyle(currentStyle.copy(autoFit = !currentStyle.autoFit)) },
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Alignment",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { updateStyle(currentStyle.copy(alignment = TextAlign.LEFT)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.FormatAlignLeft,
                    contentDescription = "Align Left",
                    tint = if (currentStyle.alignment == TextAlign.LEFT) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { updateStyle(currentStyle.copy(alignment = TextAlign.CENTER)) }) {
                Icon(
                    Icons.Filled.FormatAlignCenter,
                    contentDescription = "Align Center",
                    tint = if (currentStyle.alignment == TextAlign.CENTER) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { updateStyle(currentStyle.copy(alignment = TextAlign.RIGHT)) }) {
                Icon(
                    Icons.AutoMirrored.Filled.FormatAlignRight,
                    contentDescription = "Align Right",
                    tint = if (currentStyle.alignment == TextAlign.RIGHT) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Font Family",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val fontOptions = listOf(
                "Default" to FontFamily.DEFAULT,
                "Serif" to FontFamily.SERIF,
                "Sans" to FontFamily.SANS_SERIF,
                "Mono" to FontFamily.MONOSPACE
            )
            fontOptions.forEach { (label, family) ->
                FilterChip(
                    selected = currentStyle.fontFamily == family,
                    onClick = { updateStyle(currentStyle.copy(fontFamily = family)) },
                    label = { Text(label, fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        Text(
            text = "Color",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        val colorPresets = listOf(
            0xFFFFFFFFL to "White",
            0xFF1A1A2EL to "Dark",
            0xFF6C63FFL to "Purple",
            0xFFFF6584L to "Pink",
            0xFF3B82F6L to "Blue",
            0xFF27AE60L to "Green",
            0xFFF59E0BL to "Amber",
            0xFFE74C3CL to "Red",
            0xFFD4AF37L to "Gold",
            0xFF888888L to "Gray"
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(colorPresets.size) { index ->
                val (color, name) = colorPresets[index]
                val isSelected = currentStyle.color == color
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(color))
                        .then(
                            if (isSelected) Modifier.padding(2.dp)
                            else Modifier
                        )
                        .clickable { updateStyle(currentStyle.copy(color = color)) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(1.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTab(
    uiState: EditorState,
    viewModel: EditorViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Aspect Ratio",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        val commonRatios = listOf(
            AspectRatios.instagramPost,
            AspectRatios.instagramPortrait,
            AspectRatios.instagramStory,
            AspectRatios.youtubeThumbnail,
            AspectRatios.landscape169,
            AspectRatios.landscape43,
            AspectRatios.square,
            AspectRatios.portrait916
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val rows = commonRatios.chunked(4)
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { ratio ->
                        val isSelected = uiState.aspectRatio.id == ratio.id
                        OutlinedCard(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.updateAspectRatio(ratio)
                                },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ratio.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Text(
            text = "Export Format",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExportFormat.entries.forEach { format ->
                FilterChip(
                    selected = uiState.exportSettings.format == format,
                    onClick = {
                        viewModel.updateExportSettings(uiState.exportSettings.copy(format = format))
                    },
                    label = { Text(format.displayName) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        Text(
            text = "Export Quality",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ExportQuality.entries.forEach { quality ->
                FilterChip(
                    selected = uiState.exportSettings.quality == quality,
                    onClick = {
                        viewModel.updateExportSettings(uiState.exportSettings.copy(quality = quality))
                    },
                    label = { Text(quality.displayName) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onExport: () -> Unit,
    onShare: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onUndo, enabled = canUndo) {
                Icon(
                    Icons.AutoMirrored.Filled.Undo,
                    contentDescription = "Undo",
                    tint = if (canUndo) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
            IconButton(onClick = onRedo, enabled = canRedo) {
                Icon(
                    Icons.AutoMirrored.Filled.Redo,
                    contentDescription = "Redo",
                    tint = if (canRedo) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            }
            FilledTonalButton(onClick = onExport) {
                Icon(
                    Icons.Filled.Save,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export")
            }
            FilledTonalButton(onClick = onShare) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share")
            }
        }
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = valueDisplay(value),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = ((valueRange.endInclusive - valueRange.start) / step).toInt() - 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun ToggleSwitch(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

private suspend fun performExport(
    context: android.content.Context,
    viewModel: EditorViewModel,
    snackbarHostState: SnackbarHostState,
    isShare: Boolean
) {
    try {
        viewModel.setExporting(true)
        viewModel.saveProject()
        val state = viewModel.state.value
        val uri = ExportEngine.exportImage(
            context = context,
            state = state,
            format = state.exportSettings.format,
            quality = state.exportSettings.quality
        )

        if (uri != null) {
            if (isShare) {
                val mimeType = state.exportSettings.format.mimeType
                val shareIntent = ExportEngine.createShareIntent(context, uri, mimeType)
                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                snackbarHostState.showSnackbar("Ready to share")
            } else {
                snackbarHostState.showSnackbar("Image exported successfully")
            }
        } else {
            snackbarHostState.showSnackbar("Failed to export image")
        }
        viewModel.setExportComplete(uri != null)
    } catch (e: Exception) {
        snackbarHostState.showSnackbar("Export failed: ${e.message}")
        viewModel.setExportComplete(false)
    }
}
