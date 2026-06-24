package com.example.consecutivepractices.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Films : BottomNavItem("films", "Фильмы", Icons.Default.List)
    object Favorites : BottomNavItem("favorites", "Избранное", Icons.Default.Favorite)
    object Profile : BottomNavItem("profile", "Профиль", Icons.Default.Person)
    object Settings : BottomNavItem("settings", "Настройки", Icons.Default.Settings)
}

sealed class Screen(val route: String) {
    object FilmDetail : Screen("film_detail/{imdbId}") {
        fun createRoute(imdbId: String) = "film_detail/$imdbId"
    }
}