package com.example.quoteapp.ui.editor

import android.content.Intent
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.data.TextStylePresets
import com.example.quoteapp.data.FontCatalog
import com.example.quoteapp.data.GoogleFontProvider
import com.example.quoteapp.model.AspectRatios
import com.example.quoteapp.ui.theme.EditorLayout
import com.example.quoteapp.model.EditorTab
import com.example.quoteapp.model.EditorState
import com.example.quoteapp.model.ExportFormat
import com.example.quoteapp.model.ExportQuality
import com.example.quoteapp.model.FontFamily
import com.example.quoteapp.model.FontWeight
import com.example.quoteapp.ui.theme.AppAnim
import com.example.quoteapp.ui.theme.AppSpacing
import com.example.quoteapp.ui.theme.AppSwatchSize
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontWeight as ComposeFontWeight
import com.example.quoteapp.model.QuoteBackground
import com.example.quoteapp.model.TextAlign
import com.example.quoteapp.model.TextSettings
import com.example.quoteapp.model.TextTarget
import com.example.quoteapp.renderer.ExportEngine
import com.example.quoteapp.ui.editor.components.StyleSlider
import com.example.quoteapp.ui.editor.components.WysiwygQuotePreview
import kotlinx.coroutines.launch

private fun FontWeight.toComposeWeight(): ComposeFontWeight = ComposeFontWeight(weight)

@Composable
private fun resolveFontFamily(textStyle: TextSettings): ComposeFontFamily {
    if (textStyle.googleFontFamily != null && GoogleFontProvider.isAvailable()) {
        return GoogleFontProvider.getFontFamily(textStyle.googleFontFamily)
    }
    return when (textStyle.fontFamily) {
        FontFamily.DEFAULT -> ComposeFontFamily.Default
        FontFamily.SERIF -> ComposeFontFamily.Serif
        FontFamily.SANS_SERIF -> ComposeFontFamily.SansSerif
        FontFamily.MONOSPACE -> ComposeFontFamily.Monospace
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    projectId: String? = null,
    templateId: String? = null,
    quoteText: String? = null,
    author: String? = null,
    onNavigateBack: () -> Unit = {},
    viewModel: EditorViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val haptic = LocalHapticFeedback.current

    val uiState by viewModel.state.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val canRedo by viewModel.canRedo.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()

    var selectedTab by remember { mutableIntStateOf(1) }
    var showUnsavedDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (uiState.quote.isNotBlank() || uiState.author.isNotBlank()) {
            showUnsavedDialog = true
        } else {
            onNavigateBack()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            scope.launch { performExport(context, viewModel, snackbarHostState, isShare = false) }
        } else {
            scope.launch { snackbarHostState.showSnackbar("Storage permission required to export") }
        }
    }

    val onExportClick = {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            scope.launch { performExport(context, viewModel, snackbarHostState, isShare = false) }
        } else {
            permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initGoogleFonts()
    }

    LaunchedEffect(projectId, templateId, quoteText, author) {
        when {
            projectId != null -> viewModel.loadProject(projectId)
            templateId != null -> viewModel.loadTemplate(templateId)
            quoteText != null -> {
                viewModel.updateQuote(quoteText)
                if (!author.isNullOrBlank()) viewModel.updateAuthor(author)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quote Editor") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.quote.isNotBlank() || uiState.author.isNotBlank()) {
                            showUnsavedDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        scope.launch {
                            viewModel.saveProject()
                            snackbarHostState.showSnackbar("Project saved")
                        }
                    }) {
                        Icon(Icons.Filled.Save, contentDescription = "Save")
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
                onExport = { onExportClick() },
                onShare = {
                    scope.launch {
                        performExport(context, viewModel, snackbarHostState, isShare = true)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val isTablet = this.maxWidth >= EditorLayout.tabletBreakpoint
            val tabBar = @Composable {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    divider = {}
                ) {
                    val tabLabels = listOf(
                        "Text" to Icons.Filled.TextFields,
                        "Style" to Icons.Filled.TextFormat,
                        "Canvas" to Icons.Filled.Tune
                    )
                    tabLabels.forEachIndexed { index, (label, icon) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                viewModel.setActiveTab(
                                    when (index) {
                                        0 -> EditorTab.TEXT
                                        1 -> EditorTab.STYLE
                                        else -> EditorTab.BACKGROUND
                                    }
                                )
                            },
                            text = { Text(label, style = MaterialTheme.typography.labelMedium) },
                            icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(AppIconSizeSm)) }
                        )
                    }
                }
            }

            val tabContent = @Composable {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(tween(AppAnim.FAST_FADE)) + slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start, tween(AppAnim.FAST_FADE)
                        ) togetherWith fadeOut(tween(AppAnim.FAST_FADE))
                    },
                    label = "tab_content"
                ) { tab ->
                    when (tab) {
                        0 -> TextTab(uiState = uiState, viewModel = viewModel)
                        1 -> StyleTab(uiState = uiState, viewModel = viewModel)
                        2 -> SettingsTab(uiState = uiState, viewModel = viewModel)
                    }
                }
            }

            val previewPanel = @Composable {
                WysiwygQuotePreview(
                    uiState = uiState,
                    onPositionChange = { target, x, y ->
                        val current = if (target == TextTarget.QUOTE) uiState.quoteStyle else uiState.authorStyle
                        val updated = current.copy(positionX = x, positionY = y)
                        viewModel.updateCurrentTextStyle(updated)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppCornerRadiusLg))
                )
            }

            if (isTablet) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(AppSpacing.xl),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        previewPanel()
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        tabBar()
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(AppSpacing.lg)
                        ) {
                            tabContent()
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    if (isExporting) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = AppSpacing.lg),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.xs))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = this@BoxWithConstraints.maxHeight * 0.4f)
                    ) {
                        previewPanel()
                    }

                    tabBar()

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(AppSpacing.lg)
                    ) {
                        tabContent()
                    }
                }
            }
        }
    }

    if (showUnsavedDialog) {
        AlertDialog(
            onDismissRequest = { showUnsavedDialog = false },
            title = { Text("Unsaved Changes") },
            text = { Text("You have unsaved changes. Do you want to leave without saving?") },
            confirmButton = {
                TextButton(onClick = {
                    showUnsavedDialog = false
                    onNavigateBack()
                }) { Text("Leave") }
            },
            dismissButton = {
                TextButton(onClick = { showUnsavedDialog = false }) { Text("Stay") }
            }
        )
    }
}

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
private fun SectionDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(vertical = AppSpacing.xs),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    )
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

    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            FilterChip(
                selected = uiState.activeTextTarget == TextTarget.QUOTE,
                onClick = { viewModel.setActiveTextTarget(TextTarget.QUOTE) },
                label = { Text("Quote") },
                leadingIcon = {
                    if (uiState.activeTextTarget == TextTarget.QUOTE) {
                        Icon(Icons.Filled.TextFields, contentDescription = null, modifier = Modifier.size(AppIconSizeSm))
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
                        Icon(Icons.Filled.TextFields, contentDescription = null, modifier = Modifier.size(AppIconSizeSm))
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

    var googleFontSearch by remember { mutableStateOf("") }
    var selectedFontCategory by remember { mutableStateOf<FontCatalog.FontCategory?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
        SectionLabel("Style Presets")

        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            items(TextStylePresets.getAll()) { preset ->
                OutlinedCard(
                    modifier = Modifier
                        .width(100.dp)
                        .clickable { viewModel.applyTextStylePreset(preset) },
                    shape = RoundedCornerShape(AppCornerRadiusSm),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\"Aa\"",
                            fontSize = (preset.settings.fontSize / 4).sp,
                            fontWeight = if (preset.settings.isBold) ComposeFontWeight.Bold else ComposeFontWeight.Normal,
                            fontStyle = if (preset.settings.isItalic) FontStyle.Italic else FontStyle.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.xs))
                        Text(
                            text = preset.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        SectionDivider()

        SectionLabel(
            if (uiState.activeTextTarget == TextTarget.QUOTE) "Quote Styling" else "Author Styling"
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

        SectionDivider()

        SectionLabel("Toggles")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.lg)
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
            ToggleSwitch(
                label = "Auto Fit",
                checked = currentStyle.autoFit,
                onToggle = { updateStyle(currentStyle.copy(autoFit = !currentStyle.autoFit)) },
                modifier = Modifier.weight(1f)
            )
        }

        SectionLabel("Alignment")

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

        SectionDivider()

        SectionLabel("Font Weight")

        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            items(FontWeight.entries.toList()) { weight ->
                val isActive = currentStyle.fontWeight == weight
                FilterChip(
                    selected = isActive,
                    onClick = { updateStyle(currentStyle.copy(fontWeight = weight)) },
                    label = { Text(weight.displayName, fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        SectionLabel("Font Family")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            val fontOptions = listOf(
                "Default" to FontFamily.DEFAULT,
                "Serif" to FontFamily.SERIF,
                "Sans" to FontFamily.SANS_SERIF,
                "Mono" to FontFamily.MONOSPACE
            )
            fontOptions.forEach { (label, family) ->
                FilterChip(
                    selected = currentStyle.fontFamily == family && currentStyle.googleFontFamily == null,
                    onClick = {
                        updateStyle(currentStyle.copy(fontFamily = family, googleFontFamily = null))
                    },
                    label = { Text(label, fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        if (GoogleFontProvider.isAvailable()) {
            SectionLabel("Google Fonts")

            TextField(
                value = googleFontSearch,
                onValueChange = { googleFontSearch = it },
                label = { Text("Search fonts...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                item {
                    FilterChip(
                        selected = selectedFontCategory == null,
                        onClick = { selectedFontCategory = null },
                        label = { Text("All", fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
                items(FontCatalog.FontCategory.entries.toList()) { cat ->
                    FilterChip(
                        selected = selectedFontCategory == cat,
                        onClick = { selectedFontCategory = cat },
                        label = { Text(cat.displayName, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }

            val filteredFonts = when {
                googleFontSearch.isNotBlank() -> FontCatalog.search(googleFontSearch)
                selectedFontCategory != null -> FontCatalog.getByCategory(selectedFontCategory!!)
                else -> FontCatalog.getAll()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 180.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                filteredFonts.forEach { fontEntry ->
                    val isGoogleSelected = currentStyle.googleFontFamily == fontEntry.googleFontName
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(AppCornerRadiusSm))
                            .background(
                                if (isGoogleSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else Color.Transparent
                            )
                            .clickable {
                                updateStyle(
                                    currentStyle.copy(
                                        googleFontFamily = fontEntry.googleFontName,
                                        fontFamily = FontFamily.DEFAULT
                                    )
                                )
                            }
                            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fontEntry.displayName,
                            fontFamily = GoogleFontProvider.getFontFamily(fontEntry.googleFontName),
                            fontSize = 16.sp,
                            color = if (isGoogleSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = fontEntry.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            Text(
                text = "Google Fonts unavailable (no Play Services)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SectionDivider()

        SectionLabel("Color")

        com.example.quoteapp.ui.editor.components.ColorSwatchRow(
            selectedColor = currentStyle.color,
            onColorSelected = { color -> updateStyle(currentStyle.copy(color = color)) }
        )

        com.example.quoteapp.ui.editor.components.TextControls(
            style = currentStyle,
            onStyleChange = { updateStyle(it) }
        )
    }
}

@Composable
private fun SettingsTab(
    uiState: EditorState,
    viewModel: EditorViewModel
) {
    var bgSubTab by remember { mutableIntStateOf(0) }
    var showGradientEditor by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
        SectionLabel("Background")

        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            val bgTabs = listOf("Solid", "Gradient", "PNG", "Pattern")
            bgTabs.forEachIndexed { index, label ->
                FilterChip(
                    selected = bgSubTab == index,
                    onClick = { bgSubTab = index },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        when (bgSubTab) {
            0 -> {
                val solidColors = listOf(
                    0xFF000000L, 0xFFFFFFFFL, 0xFF1A1A2EL, 0xFF16213EL,
                    0xFF0F3460L, 0xFF533483L, 0xFF2C3E50L, 0xFF1B262CL,
                    0xFF0A1929L, 0xFF2D2D3AL, 0xFF3C1642L, 0xFF0B0C10L,
                    0xFFF5F5DCL, 0xFFFAF0E6L, 0xFF2E4057L, 0xFF1B1B2FL
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    items(solidColors.size) { index ->
                        val color = solidColors[index]
                        val isSelected = uiState.background is QuoteBackground.SolidColor &&
                            uiState.background.color == color
                        Box(
                            modifier = Modifier
                                .size(AppSwatchSize.lg)
                                .clip(RoundedCornerShape(AppCornerRadiusSm))
                                .background(Color(color))
                                .then(if (isSelected) Modifier.padding(2.dp) else Modifier)
                                .clickable { viewModel.updateBackground(QuoteBackground.SolidColor(color)) }
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
            1 -> {
                if (showGradientEditor && uiState.background is QuoteBackground.Gradient) {
                    val gradient = uiState.background as QuoteBackground.Gradient
                    TextButton(onClick = { showGradientEditor = false }) {
                        Text("\u2190 Back to presets")
                    }
                    com.example.quoteapp.ui.editor.components.GradientEditor(
                        gradient = gradient,
                        onGradientChange = { viewModel.updateBackground(it) }
                    )
                } else {
                    val presets = com.example.quoteapp.data.GradientPresets.getAll()
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                        presets.chunked(4).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                                row.forEach { preset ->
                                    val isSelected = uiState.background is QuoteBackground.Gradient &&
                                        uiState.background.colors == preset.background.colors
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clip(RoundedCornerShape(AppCornerRadiusSm))
                                            .background(
                                                androidx.compose.ui.graphics.Brush.linearGradient(
                                                    colors = preset.background.colors.map { Color(it) }
                                                )
                                            )
                                            .clickable {
                                                viewModel.updateBackground(preset.background)
                                            }
                                    )
                                }
                            }
                        }

                        TextButton(onClick = {
                            val defaultGradient = QuoteBackground.Gradient(
                                colors = listOf(0xFF667EEA, 0xFF764BA2),
                                angle = 135f
                            )
                            viewModel.updateBackground(defaultGradient)
                            showGradientEditor = true
                        }) {
                            Text("Custom Gradient")
                        }
                    }
                }
            }
            2 -> {
                com.example.quoteapp.ui.editor.components.PngBackgroundPicker(
                    onBackgroundSelected = { assetPath ->
                        viewModel.updateBackground(QuoteBackground.PngBackground(assetPath = assetPath))
                    }
                )
            }
            3 -> {
                val patterns = com.example.quoteapp.model.PatternType.entries
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    items(patterns.size) { index ->
                        val pattern = patterns[index]
                        val isSelected = uiState.background is QuoteBackground.Programmatic &&
                            uiState.background.pattern == pattern
                        OutlinedCard(
                            modifier = Modifier
                                .width(100.dp)
                                .clickable {
                                    viewModel.updateBackground(
                                        QuoteBackground.Programmatic(pattern = pattern)
                                    )
                                },
                            shape = RoundedCornerShape(AppCornerRadiusSm),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pattern.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        SectionDivider()

        SectionLabel("Aspect Ratio")

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

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            val rows = commonRatios.chunked(4)
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    row.forEach { ratio ->
                        val isSelected = uiState.aspectRatio.id == ratio.id
                        OutlinedCard(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.updateAspectRatio(ratio)
                                },
                            shape = RoundedCornerShape(AppCornerRadiusSm),
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
                                    .padding(AppSpacing.md),
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

        SectionDivider()

        SectionLabel("Export Format")

        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
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

        SectionLabel("Export Quality")

        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
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

        Spacer(modifier = Modifier.height(AppSpacing.sm))

        var showResetDialog by remember { mutableStateOf(false) }

        TextButton(
            onClick = { showResetDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Reset to Default",
                color = MaterialTheme.colorScheme.error
            )
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Editor") },
                text = { Text("This will reset all settings to default. This cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.reset()
                        showResetDialog = false
                    }) { Text("Reset", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
                }
            )
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
                .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
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
            }
            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                FilledTonalButton(onClick = onExport) {
                    Icon(
                        Icons.Filled.FileDownload,
                        contentDescription = null,
                        modifier = Modifier.size(AppIconSizeSm)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.xs))
                    Text("Export")
                }
                FilledTonalButton(onClick = onShare) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = null,
                        modifier = Modifier.size(AppIconSizeSm)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.xs))
                    Text("Share")
                }
            }
        }
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

private val AppIconSizeSm = 18.dp
private val AppCornerRadiusSm = 8.dp
private val AppCornerRadiusLg = 12.dp

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
