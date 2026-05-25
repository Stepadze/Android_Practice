package com.example.consecutivepractices.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.consecutivepractices.ui.favorites.FavoritesScreen
import com.example.consecutivepractices.ui.films.FilmApp
import com.example.consecutivepractices.ui.films.FilmsViewModel
import com.example.consecutivepractices.ui.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Films : Screen("films", "Фильмы", Icons.AutoMirrored.Filled.List)
    object Favorites : Screen("favorites", "Избранное", Icons.Default.Favorite)
    object Settings : Screen("settings", "Настройки", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val filmsViewModel: FilmsViewModel = koinViewModel()
    val showBadge by filmsViewModel.showBadge.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val screens = listOf(Screen.Films, Screen.Favorites, Screen.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            if (screen == Screen.Settings && showBadge) {
                                BadgedBox(badge = { Badge() }) {
                                    Icon(screen.icon, contentDescription = screen.label)
                                }
                            } else {
                                Icon(screen.icon, contentDescription = screen.label)
                            }
                        },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
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
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Films.route,
            modifier = Modifier
        ) {
            composable(Screen.Films.route) {
                FilmApp(paddingValues = padding)
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onFilmClick = { imdbId ->
                        // Здесь можно добавить навигацию на детали фильма
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onApply = { query, type, year ->
                        filmsViewModel.searchFilms(query, type, year)
                        val hasFilters = query != "Batman" || type != "movie" || year.isNotEmpty()
                        filmsViewModel.updateBadge(hasFilters)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}