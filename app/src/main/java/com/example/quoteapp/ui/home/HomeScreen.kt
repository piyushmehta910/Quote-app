package com.example.quoteapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.data.TemplateLibrary
import com.example.quoteapp.model.Quote
import com.example.quoteapp.model.QuoteCategory
import com.example.quoteapp.model.QuoteTemplate
import com.example.quoteapp.model.Project
import com.example.quoteapp.ui.theme.AppAnim
import com.example.quoteapp.ui.theme.AppCornerRadius
import com.example.quoteapp.ui.theme.AppSpacing
import com.example.quoteapp.ui.theme.isDarkModeOverride
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToEditor: (String?) -> Unit = {},
    onNavigateToTemplates: (categoryName: String?) -> Unit = { _ -> },
    onNavigateToEditorWithQuote: (String, String) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = viewModel()
) {
    val randomQuote by viewModel.randomQuote.collectAsState()
    val recentProjects by viewModel.recentProjects.collectAsState()

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.getRandomQuote()
    }

    val categories = listOf(
        QuoteCategory.MOTIVATION to "Motivation",
        QuoteCategory.SUCCESS to "Success",
        QuoteCategory.LIFE to "Life",
        QuoteCategory.LOVE to "Love",
        QuoteCategory.FRIENDSHIP to "Friendship",
        QuoteCategory.STUDY to "Study",
        QuoteCategory.BUSINESS to "Business",
        QuoteCategory.FITNESS to "Fitness",
        QuoteCategory.CONFIDENCE to "Confidence"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xxl),
        contentPadding = PaddingValues(top = AppSpacing.xxl, bottom = AppSpacing.xxxl)
    ) {
        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(AppAnim.FADE_DURATION)) + slideInVertically(tween(AppAnim.SLIDE_DURATION)) { it / 2 }
            ) {
                HeaderSection()
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY)) { it / 2 }
            ) {
                CreateQuoteCard(onClick = { onNavigateToEditor(null) })
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 2)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 2)) { it / 2 }
            ) {
                RandomQuoteSection(
                    quote = randomQuote,
                    onUseQuote = { quote ->
                        onNavigateToEditorWithQuote(quote.text, quote.author)
                    },
                    onNewQuote = { viewModel.getRandomQuote() }
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 3)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 3)) { it / 2 }
            ) {
                TemplatesSection(
                    templates = TemplateLibrary.templates.take(8),
                    onTemplateClick = { templateId -> onNavigateToEditor(templateId) },
                    onSeeAllClick = { onNavigateToTemplates(null) }
                )
            }
        }

        item {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 4)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 4)) { it / 2 }
            ) {
                QuickCategoriesSection(
                    categories = categories,
                    onCategoryClick = { categoryName -> onNavigateToTemplates(categoryName) }
                )
            }
        }

        if (recentProjects.isNotEmpty()) {
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 5)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = AppAnim.STAGGER_DELAY * 5)) { it / 2 }
                ) {
                    RecentProjectsSection(
                        projects = recentProjects.take(5),
                        onProjectClick = { project -> onNavigateToEditor(project.templateId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Quote Studio",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Create beautiful quote images",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        val isDark = isDarkModeOverride ?: isSystemInDarkTheme()
        IconButton(onClick = { isDarkModeOverride = !isDark }) {
            Icon(
                imageVector = if (isDark) Icons.Filled.SettingsBrightness else Icons.Filled.DarkMode,
                contentDescription = "Toggle theme",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CreateQuoteCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(AppCornerRadius.xxl))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppCornerRadius.xxl),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Create Quote",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Start from scratch or pick a template",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RandomQuoteSection(
    quote: Quote?,
    onUseQuote: (Quote) -> Unit,
    onNewQuote: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Inspiration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = onNewQuote) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "New Quote",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (quote != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppCornerRadius.xl),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = quote.text,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    lineHeight = 24.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\u2014 ${quote.author}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilledTonalButton(
                        onClick = { onUseQuote(quote) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppCornerRadius.md)
                    ) {
                        Text("Use This Quote")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppCornerRadius.xl),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading inspiration...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplatesSection(
    templates: List<QuoteTemplate>,
    onTemplateClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Templates",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "See All",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            itemsIndexed(templates, key = { _, t -> t.id }) { index, template ->
                TemplatePreviewCard(
                    template = template,
                    onClick = { onTemplateClick(template.id) },
                    enterDelay = index * 50
                )
            }
        }
    }
}

@Composable
private fun TemplatePreviewCard(
    template: QuoteTemplate,
    onClick: () -> Unit,
    enterDelay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(AppAnim.FADE_DURATION, delayMillis = enterDelay)) + slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = enterDelay)) { it / 3 }
    ) {
        Card(
            modifier = Modifier
                .width(com.example.quoteapp.ui.theme.AppCardSize.templateCardWidth)
                .height(150.dp)
                .clip(RoundedCornerShape(AppCornerRadius.lg))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(AppCornerRadius.lg),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = Color(template.thumbnailColor),
                            shape = RoundedCornerShape(topStart = AppCornerRadius.lg, topEnd = AppCornerRadius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = template.name.take(1),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.3f)
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
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickCategoriesSection(
    categories: List<Pair<QuoteCategory, String>>,
    onCategoryClick: (String) -> Unit
) {
    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(categories, key = { it.first.name }) { (category, displayName) ->
                FilterChip(
                    selected = false,
                    onClick = { onCategoryClick(displayName) },
                    label = { Text(displayName) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(AppCornerRadius.xxl)
                )
            }
        }
    }
}

@Composable
private fun RecentProjectsSection(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit
) {
    Column {
        Text(
            text = "Recent",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            projects.forEach { project ->
                ProjectCard(
                    project = project,
                    onClick = { onProjectClick(project) }
                )
            }
        }
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppCornerRadius.md))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppCornerRadius.md),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formatDate(project.updatedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            if (project.quote.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.quote,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
