package com.example.practice3final.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.usecase.GetAllFilmsUseCase
import com.example.practice3final.domain.usecase.GetFilmByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FilmsUiState {
    object Loading : FilmsUiState()
    data class Success(val films: List<Film>) : FilmsUiState()
    data class Error(val message: String) : FilmsUiState()
}

sealed class FilmDetailUiState {
    object Loading : FilmDetailUiState()
    data class Success(val film: Film) : FilmDetailUiState()
    data class Error(val message: String) : FilmDetailUiState()
}

class FilmsViewModel(
    private val getAllFilmsUseCase: GetAllFilmsUseCase,
    private val getFilmByIdUseCase: GetFilmByIdUseCase
) : ViewModel() {

    private val _filmsState = MutableStateFlow<FilmsUiState>(FilmsUiState.Loading)
    val filmsState: StateFlow<FilmsUiState> = _filmsState.asStateFlow()

    private val _filmDetailState = MutableStateFlow<FilmDetailUiState>(FilmDetailUiState.Loading)
    val filmDetailState: StateFlow<FilmDetailUiState> = _filmDetailState.asStateFlow()

    fun loadAllFilms() {
        viewModelScope.launch {
            println("DEBUG VM: Setting Loading state")
            _filmsState.value = FilmsUiState.Loading
            try {
                println("DEBUG VM: Calling getAllFilmsUseCase")
                val films = getAllFilmsUseCase()
                println("DEBUG VM: Got ${films.size} films")
                _filmsState.value = FilmsUiState.Success(films)
            } catch (e: Exception) {
                println("DEBUG VM: Error: ${e.message}")
                _filmsState.value = FilmsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadFilmById(id: String) {
        viewModelScope.launch {
            _filmDetailState.value = FilmDetailUiState.Loading
            try {
                val film = getFilmByIdUseCase(id)
                if (film != null) {
                    _filmDetailState.value = FilmDetailUiState.Success(film)
                } else {
                    _filmDetailState.value = FilmDetailUiState.Error("Film not found")
                }
            } catch (e: Exception) {
                _filmDetailState.value = FilmDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

