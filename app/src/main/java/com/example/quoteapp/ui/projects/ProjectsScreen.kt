package com.example.quoteapp.ui.projects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quoteapp.model.Project
import com.example.quoteapp.ui.components.EmptyState
import com.example.quoteapp.ui.components.ScreenHeader
import com.example.quoteapp.ui.theme.AppAnim
import com.example.quoteapp.ui.theme.AppCornerRadius
import com.example.quoteapp.ui.theme.AppIconSize
import com.example.quoteapp.ui.theme.AppSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsScreen(
    onNavigateToEditor: (projectId: String) -> Unit,
    viewModel: ProjectsViewModel = viewModel()
) {
    val projects by viewModel.projects.collectAsState()
    var projectToDelete by remember { mutableStateOf<Project?>(null) }
    var showRenameDialog by remember { mutableStateOf<Project?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            title = "My Projects",
            subtitle = "${projects.size} project${if (projects.size != 1) "s" else ""}"
        )

        if (projects.isEmpty()) {
            EmptyState(
                icon = Icons.Default.FolderOpen,
                title = "No projects yet",
                subtitle = "Create a quote to get started"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(AppSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                itemsIndexed(projects, key = { _, p -> p.id }) { index, project ->
                    ProjectCard(
                        project = project,
                        onClick = { onNavigateToEditor(project.id) },
                        onFavorite = { viewModel.toggleFavorite(project.id) },
                        onDelete = { projectToDelete = project },
                        onRename = { showRenameDialog = project },
                        enterDelay = index * AppAnim.STAGGER_DELAY
                    )
                }
            }
        }
    }

    projectToDelete?.let { project ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text("Delete Project") },
            text = { Text("Delete \"${project.name}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProject(project.id)
                    projectToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) { Text("Cancel") }
            }
        )
    }

    showRenameDialog?.let { project ->
        var newName by remember { mutableStateOf(project.name) }
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Rename Project") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Project name") },
                    singleLine = true,
                    shape = RoundedCornerShape(AppCornerRadius.md)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isNotBlank()) {
                        viewModel.renameProject(project.id, newName.trim())
                    }
                    showRenameDialog = null
                }) { Text("Rename") }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onFavorite: () -> Unit,
    onDelete: () -> Unit,
    onRename: () -> Unit,
    enterDelay: Int = 0
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(AppAnim.FADE_DURATION, delayMillis = enterDelay)) +
                slideInVertically(tween(AppAnim.SLIDE_DURATION, delayMillis = enterDelay)) { it / 4 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(AppCornerRadius.lg)
        ) {
            Column(modifier = Modifier.padding(AppSpacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        project.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                        IconButton(onClick = onFavorite, modifier = Modifier.size(40.dp)) {
                            Icon(
                                if (project.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (project.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(AppIconSize.md)
                            )
                        }
                        IconButton(onClick = onRename, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Rename", modifier = Modifier.size(AppIconSize.sm))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(AppIconSize.sm))
                        }
                    }
                }
                if (project.quote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(
                        project.quote,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (project.author.isNotBlank()) {
                    Spacer(modifier = Modifier.height(AppSpacing.xxs))
                    Text(
                        "\u2014 ${project.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        dateFormat.format(Date(project.updatedAt)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
