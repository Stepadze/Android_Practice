package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.model.FilmShort
import com.example.consecutivepractices.domain.repository.FilmRepository

class SearchFilmsUseCase(private val repository: FilmRepository) {
    suspend operator fun invoke(query: String): List<FilmShort> {
        return repository.searchFilms(query)
    }
}