package com.example.quoteapp.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quoteapp.ui.editor.EditorScreen
import com.example.quoteapp.ui.favorites.FavoritesScreen
import com.example.quoteapp.ui.home.HomeScreen
import com.example.quoteapp.ui.projects.ProjectsScreen
import com.example.quoteapp.ui.templates.TemplatesScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Editor : Screen("editor?projectId={projectId}&templateId={templateId}&quoteText={quoteText}") {
        fun createRoute(projectId: String? = null, templateId: String? = null, quoteText: String? = null): String {
            return buildString {
                append("editor")
                val params = mutableListOf<String>()
                projectId?.let { params.add("projectId=$it") }
                templateId?.let { params.add("templateId=$it") }
                quoteText?.let { params.add("quoteText=$it") }
                if (params.isNotEmpty()) {
                    append("?${params.joinToString("&")}")
                }
            }
        }
    }
    data object Templates : Screen("templates")
    data object Favorites : Screen("favorites")
    data object Projects : Screen("projects")
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Screen.Home),
    BottomNavItem("Projects", Icons.Default.Folder, Screen.Projects),
    BottomNavItem("Templates", Icons.Default.Style, Screen.Templates),
    BottomNavItem("Favorites", Icons.Default.Favorite, Screen.Favorites),
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Projects.route,
        Screen.Templates.route,
        Screen.Favorites.route
    )

    val showBottomBar = currentDestination?.route?.let { route ->
        bottomBarRoutes.any { route.startsWith(it) }
    } ?: false

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(tween(200)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(250)
                )
            },
            exitTransition = {
                fadeOut(tween(200))
            },
            popEnterTransition = {
                fadeIn(tween(200)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(250)
                )
            },
            popExitTransition = {
                fadeOut(tween(200))
            }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToEditor = { templateId ->
                        val route = Screen.Editor.createRoute(templateId = templateId)
                        navController.navigate(route)
                    },
                    onNavigateToTemplates = {
                        navController.navigate(Screen.Templates.route)
                    },
                    onNavigateToEditorWithQuote = { quoteText, author ->
                        val route = Screen.Editor.createRoute(quoteText = quoteText)
                        navController.navigate(route)
                    }
                )
            }

            composable(
                route = Screen.Editor.route,
                arguments = listOf(
                    navArgument("projectId") { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument("templateId") { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument("quoteText") { type = NavType.StringType; nullable = true; defaultValue = null },
                )
            ) { backStackEntry ->
                EditorScreen(
                    projectId = backStackEntry.arguments?.getString("projectId"),
                    templateId = backStackEntry.arguments?.getString("templateId"),
                    quoteText = backStackEntry.arguments?.getString("quoteText"),
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Templates.route) {
                TemplatesScreen(
                    onNavigateToEditor = { templateId ->
                        val route = Screen.Editor.createRoute(templateId = templateId)
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onNavigateToEditor = { quoteText, author ->
                        val route = Screen.Editor.createRoute(quoteText = quoteText)
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.Projects.route) {
                ProjectsScreen(
                    onNavigateToEditor = { projectId ->
                        val route = Screen.Editor.createRoute(projectId = projectId)
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}
