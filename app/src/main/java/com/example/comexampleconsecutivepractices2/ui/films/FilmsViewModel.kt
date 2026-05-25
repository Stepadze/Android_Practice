package com.example.consecutivepractices.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.database.FavoriteFilmDao
import com.example.consecutivepractices.data.database.FavoriteFilmEntity
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.usecase.SearchFilmsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class FilmsUiState {
    object Loading : FilmsUiState()
    data class Success(val films: List<Film>) : FilmsUiState()
    data class Error(val message: String) : FilmsUiState()
}

class FilmsViewModel(
    private val searchFilmsUseCase: SearchFilmsUseCase,
    private val filterBadgeCache: FilterBadgeCache,
    private val searchPreferences: SearchPreferences,
    private val favoriteFilmDao: FavoriteFilmDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmsUiState>(FilmsUiState.Loading)
    val uiState: StateFlow<FilmsUiState> = _uiState.asStateFlow()

    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge.asStateFlow()

    init {
        loadSavedSettingsAndSearch()
    }

    fun searchFilms(query: String, type: String? = null, year: String? = null) {
        _uiState.value = FilmsUiState.Loading
        viewModelScope.launch {
            try {
                val films = searchFilmsUseCase(query, type, year)
                _uiState.value = FilmsUiState.Success(films)
            } catch (e: Exception) {
                _uiState.value = FilmsUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun updateBadge(hasFilters: Boolean) {
        filterBadgeCache.setHasActiveFilters(hasFilters)
        _showBadge.value = hasFilters
    }

    private fun loadSavedSettingsAndSearch() {
        viewModelScope.launch {
            val settings = searchPreferences.searchSettings.first()
            searchFilms(settings.query, settings.type, settings.year)
            val hasFilters = settings.query != "Batman" || settings.type != "movie" || settings.year.isNotEmpty()
            updateBadge(hasFilters)
        }
    }

    fun addToFavorites(film: Film) {
        viewModelScope.launch {
            favoriteFilmDao.addToFavorites(
                FavoriteFilmEntity(
                    imdbId = film.imdbId,
                    title = film.title,
                    year = film.year,
                    posterUrl = film.posterUrl
                )
            )
        }
    }

    fun removeFromFavorites(imdbId: String) {
        viewModelScope.launch {
            favoriteFilmDao.removeFromFavorites(imdbId)
        }
    }

    fun isFavorite(imdbId: String): Flow<Boolean> = favoriteFilmDao.isFavorite(imdbId)
}