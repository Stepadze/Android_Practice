package com.example.consecutivepractices.ui.films

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.database.FavoriteFilmDao
import com.example.consecutivepractices.data.preferences.SearchPreferences
import com.example.consecutivepractices.domain.cache.FilterBadgeCache
import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.model.FilmShort
import com.example.consecutivepractices.domain.usecase.GetFilmByIdUseCase
import com.example.consecutivepractices.domain.usecase.SearchFilmsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.consecutivepractices.data.database.FavoriteFilmEntity


sealed class FilmsUiState {
    object Loading : FilmsUiState()
    data class Success(val films: List<FilmShort>) : FilmsUiState()
    data class Error(val message: String) : FilmsUiState()
}

sealed class FilmDetailUiState {
    object Loading : FilmDetailUiState()
    data class Success(val film: Film) : FilmDetailUiState()
    data class Error(val message: String) : FilmDetailUiState()
}

class FilmsViewModel(
    private val searchFilmsUseCase: SearchFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val favoriteFilmDao: FavoriteFilmDao,
    private val filterBadgeCache: FilterBadgeCache
) : ViewModel() {

    private val _filmsState = MutableStateFlow<FilmsUiState>(FilmsUiState.Loading)
    val filmsState: StateFlow<FilmsUiState> = _filmsState

    private val _filmDetailState = MutableStateFlow<FilmDetailUiState>(FilmDetailUiState.Loading)
    val filmDetailState: StateFlow<FilmDetailUiState> = _filmDetailState

    private val _searchQuery = MutableStateFlow("Batman")
    val searchQuery: StateFlow<String> = _searchQuery

    val _searchType = MutableStateFlow("movie") // инициализация
    val _searchYear = MutableStateFlow("") // инициализация
    // Публичные свойства для фильтров
    val searchType: StateFlow<String> = _searchType
    val searchYear: StateFlow<String> = _searchYear

    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge

    init {
        searchFilms("Batman", "movie", "")
        _showBadge.value = filterBadgeCache.getHasActiveFilters()
    }

    fun searchFilms(query: String, type: String, year: String) {
        _searchQuery.value = query
        _searchType.value = type
        _searchYear.value = year
        _filmsState.value = FilmsUiState.Loading

        viewModelScope.launch {
            try {
                val films = searchFilmsUseCase(query, type, year)
                _filmsState.value = FilmsUiState.Success(films)
            } catch (e: Exception) {
                _filmsState.value = FilmsUiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun loadFilmDetail(imdbId: String) {
        _filmDetailState.value = FilmDetailUiState.Loading
        viewModelScope.launch {
            try {
                val film = getFilmByIdUseCase(imdbId)
                _filmDetailState.value = FilmDetailUiState.Success(film)
            } catch (e: Exception) {
                _filmDetailState.value = FilmDetailUiState.Error(e.message ?: "Неизвестная ошибка")
            }
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
            favoriteFilmDao.removeFromFavorites(FavoriteFilmEntity(imdbId, "", "", ""))
        }
    }

    fun isFavorite(imdbId: String): Flow<Boolean> = favoriteFilmDao.isFavorite(imdbId)

    fun updateBadge() {
        _showBadge.value = filterBadgeCache.getHasActiveFilters()
    }
}
