package com.example.quoteapp.ui.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteTemplate

@Composable
fun FavoritesScreen(
    onNavigateToEditor: (quoteText: String, author: String) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildString {
                    if (favoriteQuotes.isNotEmpty()) append("${favoriteQuotes.size} quotes")
                    if (favoriteQuotes.isNotEmpty() && favoriteTemplates.isNotEmpty()) append(" \u00B7 ")
                    if (favoriteTemplates.isNotEmpty()) append("${favoriteTemplates.size} templates")
                    if (favoriteQuotes.isEmpty() && favoriteTemplates.isEmpty()) append("No favorites yet")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (favoriteQuotes.isEmpty() && favoriteTemplates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No favorites yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Heart quotes and templates to save them here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(favoriteTemplates, key = { _, t -> t.id }) { index, template ->
                                FavoriteTemplateCard(
                                    template = template,
                                    onClick = { onNavigateToEditor("", "") },
                                    enterDelay = index * 50
                                )
                            }
                        }
                    }
                }

                if (favoriteQuotes.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
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
                            enterDelay = index * 30
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
        enter = fadeIn(tween(250, delayMillis = enterDelay)) + slideInVertically(tween(250, delayMillis = enterDelay)) { it / 3 }
    ) {
        Card(
            modifier = Modifier
                .width(120.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column {
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
                        .padding(8.dp),
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
}

@Composable
private fun FavoriteQuoteCard(
    quote: Quote,
    onUse: () -> Unit,
    onRemove: () -> Unit,
    enterDelay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(250, delayMillis = enterDelay)) + slideInVertically(tween(250, delayMillis = enterDelay)) { it / 4 }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    quote.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "\u2014 ${quote.author}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = onUse,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Use This")
                    }
                    IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
