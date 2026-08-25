package com.example.quoteapp.ui.templates

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.model.QuoteTemplate
import com.example.quoteapp.model.TemplateCategory
import com.example.quoteapp.ui.components.EmptyState
import com.example.quoteapp.ui.components.ScreenHeader
import com.example.quoteapp.ui.theme.AppAnim
import com.example.quoteapp.ui.theme.AppCornerRadius
import com.example.quoteapp.ui.theme.AppIconSize
import com.example.quoteapp.ui.theme.AppSpacing

@Composable
fun TemplatesScreen(
    onNavigateToEditor: (templateId: String) -> Unit,
    viewModel: TemplatesViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<TemplateCategory?>(null) }

    val templates by viewModel.templates.collectAsState()
    val favoriteIds by viewModel.favoriteTemplateIds.collectAsState()

    val filteredTemplates = remember(templates, searchQuery, selectedCategory) {
        templates.filter { template ->
            val matchesSearch = if (searchQuery.isBlank()) true
            else template.name.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory?.let {
                template.category == it
            } ?: true
            matchesSearch && matchesCategory
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            title = "Templates",
            subtitle = "${templates.size} templates available"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.lg)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search templates...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppCornerRadius.lg),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("All") },
                    shape = RoundedCornerShape(AppCornerRadius.md)
                )
                TemplateCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.displayName) },
                        shape = RoundedCornerShape(AppCornerRadius.md)
                    )
                }
            }
        }

        if (filteredTemplates.isEmpty()) {
            EmptyState(
                icon = Icons.Default.SearchOff,
                title = "No templates found",
                subtitle = "Try a different search or category"
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                itemsIndexed(filteredTemplates, key = { _, t -> t.id }) { index, template ->
                    TemplateCard(
                        template = template,
                        isFavorited = template.id in favoriteIds,
                        onClick = { onNavigateToEditor(template.id) },
                        onFavoriteToggle = { viewModel.toggleFavorite(template.id) },
                        enterDelay = index * AppAnim.STAGGER_DELAY
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: QuoteTemplate,
    isFavorited: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    enterDelay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(AppAnim.FADE_DURATION, delayMillis = enterDelay)) +
                slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = enterDelay)) { it / 4 }
    ) {
        Card(
            template = template,
            isFavorited = isFavorited,
            onClick = onClick,
            onFavoriteToggle = onFavoriteToggle
        )
    }
}

@Composable
private fun Card(
    template: QuoteTemplate,
    isFavorited: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(AppCornerRadius.lg))
            .clickable { onClick() }
    ) {
        Box(
            template = template,
            isFavorited = isFavorited,
            onFavoriteToggle = onFavoriteToggle
        )
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(AppSpacing.xxs))
            Text(
                text = template.category.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun Box(
    template: QuoteTemplate,
    isFavorited: Boolean,
    onFavoriteToggle: () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Color(template.thumbnailColor)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = template.name.take(1),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.2f)
        )

        if (template.isPremium) {
            androidx.compose.material3.Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(AppSpacing.sm),
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.tertiary
            ) {
                Text(
                    text = "PRO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        IconButton(
            onClick = onFavoriteToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(36.dp)
        ) {
            Icon(
                imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorited) MaterialTheme.colorScheme.error else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(AppIconSize.md)
            )
        }
    }
}
