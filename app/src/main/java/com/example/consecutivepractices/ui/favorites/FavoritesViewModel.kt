package com.example.consecutivepractices.ui.favorites

import androidx.lifecycle.ViewModel
import com.example.consecutivepractices.data.database.FavoriteFilmDao
import com.example.consecutivepractices.data.database.FavoriteFilmEntity
import kotlinx.coroutines.flow.Flow

class FavoritesViewModel(
    private val favoriteFilmDao: FavoriteFilmDao
) : ViewModel() {
    val favorites: Flow<List<FavoriteFilmEntity>> = favoriteFilmDao.getAllFavorites()
}