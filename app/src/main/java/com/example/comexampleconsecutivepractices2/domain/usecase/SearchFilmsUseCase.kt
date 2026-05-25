package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.repository.FilmRepository

class SearchFilmsUseCase(private val repository: FilmRepository) {
    suspend operator fun invoke(query: String, type: String? = null, year: String? = null): List<Film> {
        return repository.searchFilms(query, type, year)
    }
}