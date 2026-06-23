package com.example.practice3final.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice3final.data.database.FavoriteFilmDao
import com.example.practice3final.data.database.FavoriteFilmEntity
import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.usecase.GetFilmByIdUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class FilmDetailUiState {
    object Loading : FilmDetailUiState()
    data class Success(val film: Film) : FilmDetailUiState()
    data class Error(val message: String) : FilmDetailUiState()
}

class FilmDetailViewModel(
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val favoriteFilmDao: FavoriteFilmDao
) : ViewModel() {

    private val _detailState = MutableStateFlow<FilmDetailUiState>(FilmDetailUiState.Loading)
    val detailState: StateFlow<FilmDetailUiState> = _detailState.asStateFlow()

    fun loadFilmDetail(imdbId: String) {
        _detailState.value = FilmDetailUiState.Loading
        viewModelScope.launch {
            try {
                val film = getFilmByIdUseCase(imdbId)
                _detailState.value = FilmDetailUiState.Success(film)
            } catch (e: Exception) {
                _detailState.value = FilmDetailUiState.Error(e.message ?: "Ошибка загрузки")
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
            favoriteFilmDao.removeFromFavorites(imdbId)
        }
    }

    fun isFavorite(imdbId: String): Flow<Boolean> = favoriteFilmDao.isFavorite(imdbId)
}