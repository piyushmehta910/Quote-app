package com.example.quoteapp.ui.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteTemplate
import com.example.quoteapp.ui.components.EmptyState
import com.example.quoteapp.ui.components.ScreenHeader
import com.example.quoteapp.ui.theme.AppAnim
import com.example.quoteapp.ui.theme.AppCornerRadius
import com.example.quoteapp.ui.theme.AppIconSize
import com.example.quoteapp.ui.theme.AppSpacing

@Composable
fun FavoritesScreen(
    onNavigateToEditor: (quoteText: String, author: String) -> Unit,
    onNavigateToTemplate: (templateId: String) -> Unit = {},
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoriteQuotes by viewModel.favoriteQuotes.collectAsState()
    val favoriteTemplateIds by viewModel.favoriteTemplateIds.collectAsState()
    val favoriteTemplates by viewModel.favoriteTemplates.collectAsState()

    LaunchedEffect(favoriteTemplateIds) {
        viewModel.loadFavoriteQuotes()
        viewModel.loadFavoriteTemplates()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        val subtitle = buildString {
            if (favoriteQuotes.isNotEmpty()) append("${favoriteQuotes.size} quotes")
            if (favoriteQuotes.isNotEmpty() && favoriteTemplates.isNotEmpty()) append(" \u00B7 ")
            if (favoriteTemplates.isNotEmpty()) append("${favoriteTemplates.size} templates")
            if (favoriteQuotes.isEmpty() && favoriteTemplates.isEmpty()) append("No favorites yet")
        }
        ScreenHeader(title = "Favorites", subtitle = subtitle)

        if (favoriteQuotes.isEmpty() && favoriteTemplates.isEmpty()) {
            EmptyState(
                icon = Icons.Default.FavoriteBorder,
                title = "No favorites yet",
                subtitle = "Heart quotes and templates to save them here"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
            ) {
                if (favoriteTemplates.isNotEmpty()) {
                    item {
                        Text(
                            "Favorite Templates",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(AppSpacing.lg)) {
                            itemsIndexed(favoriteTemplates, key = { _, t -> t.id }) { index, template ->
                                FavoriteTemplateCard(
                                    template = template,
                                    onClick = { onNavigateToTemplate(template.id) },
                                    enterDelay = index * AppAnim.STAGGER_DELAY
                                )
                            }
                        }
                    }
                }

                if (favoriteQuotes.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                        Text(
                            "Favorite Quotes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    itemsIndexed(favoriteQuotes, key = { _, q -> q.id }) { index, quote ->
                        FavoriteQuoteCard(
                            quote = quote,
                            onUse = { onNavigateToEditor(quote.text, quote.author) },
                            onRemove = { viewModel.toggleFavoriteQuote(quote.id) },
                            enterDelay = index * AppAnim.STAGGER_DELAY
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteTemplateCard(
    template: QuoteTemplate,
    onClick: () -> Unit,
    enterDelay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(AppAnim.FADE_DURATION, delayMillis = enterDelay)) +
                slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = enterDelay)) { it / 3 }
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(AppCornerRadius.lg))
                .clickable(onClick = onClick)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(template.thumbnailColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = template.name.take(1),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.2f)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(AppSpacing.sm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FavoriteQuoteCard(
    quote: Quote,
    onUse: () -> Unit,
    onRemove: () -> Unit,
    enterDelay: Int = 0
) {
    val haptic = LocalHapticFeedback.current
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(AppAnim.FADE_DURATION, delayMillis = enterDelay)) +
                slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = enterDelay)) { it / 4 }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppCornerRadius.lg)
        ) {
            Column(modifier = Modifier.padding(AppSpacing.lg)) {
                Text(
                    quote.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    "\u2014 ${quote.author}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(AppSpacing.md))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = onUse,
                        shape = RoundedCornerShape(AppCornerRadius.md)
                    ) {
                        Text("Use This")
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onRemove()
                    }, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(AppIconSize.md)
                        )
                    }
                }
            }
        }
    }
}
