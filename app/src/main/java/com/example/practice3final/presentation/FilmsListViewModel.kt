package com.example.practice3final.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.usecase.SearchFilmsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FilmsListUiState {
    object Loading : FilmsListUiState()
    data class Success(val films: List<Film>) : FilmsListUiState()
    data class Error(val message: String) : FilmsListUiState()
}

class FilmsListViewModel(
    private val searchFilmsUseCase: SearchFilmsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmsListUiState>(FilmsListUiState.Loading)
    val uiState: StateFlow<FilmsListUiState> = _uiState.asStateFlow()

    init {
        searchFilms("Batman")
    }

    fun searchFilms(query: String, type: String? = null, year: String? = null) {
        _uiState.value = FilmsListUiState.Loading
        viewModelScope.launch {
            try {
                val films = searchFilmsUseCase(query, type, year)
                _uiState.value = FilmsListUiState.Success(films)
            } catch (e: Exception) {
                _uiState.value = FilmsListUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}