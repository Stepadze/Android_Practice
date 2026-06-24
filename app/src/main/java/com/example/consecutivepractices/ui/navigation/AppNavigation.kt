package com.example.consecutivepractices.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.consecutivepractices.ui.favorites.FavoritesScreen
import com.example.consecutivepractices.ui.films.FilmDetailScreen
import com.example.consecutivepractices.ui.films.FilmsListScreen
import com.example.consecutivepractices.ui.films.FilmsViewModel
import com.example.consecutivepractices.ui.profile.EditProfileScreen
import com.example.consecutivepractices.ui.profile.ProfileScreen
import com.example.consecutivepractices.ui.settings.SettingsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val filmsViewModel: FilmsViewModel = koinViewModel()

    val bottomNavItems = listOf(
        BottomNavItem.Films,
        BottomNavItem.Favorites,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Скрываем нижнюю навигацию на экранах деталей и редактирования
            val hideBottomBar = currentDestination?.route?.startsWith("film_detail") == true
                    || currentDestination?.route == "edit_profile"

            if (!hideBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val showBadge by filmsViewModel.showBadge.collectAsState()
                        NavigationBarItem(
                            icon = {
                                if (item is BottomNavItem.Settings && showBadge) {
                                    BadgedBox(badge = { Badge() }) {
                                        Icon(item.icon, contentDescription = item.label)
                                    }
                                } else {
                                    Icon(item.icon, contentDescription = item.label)
                                }
                            },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
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
            startDestination = BottomNavItem.Films.route,
            modifier = Modifier
        ) {
            composable(BottomNavItem.Films.route) {
                FilmsListScreen(
                    onFilmClick = { imdbId ->
                        navController.navigate(Screen.FilmDetail.createRoute(imdbId))
                    },
                    viewModel = filmsViewModel
                )
            }

            composable(
                route = Screen.FilmDetail.route,
                arguments = listOf(navArgument("imdbId") { type = NavType.StringType })
            ) { backStackEntry ->
                val imdbId = backStackEntry.arguments?.getString("imdbId") ?: return@composable
                FilmDetailScreen(
                    imdbId = imdbId,
                    onBack = { navController.popBackStack() },
                    viewModel = filmsViewModel
                )
            }

            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen()
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onEditClick = { navController.navigate("edit_profile") }
                )
            }

            composable("edit_profile") {
                EditProfileScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    onApply = { query ->
                        filmsViewModel.searchFilms(query)
                        filmsViewModel.updateBadge()
                        navController.navigate(BottomNavItem.Films.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                        }
                    }
                )
            }
        }
    }
}