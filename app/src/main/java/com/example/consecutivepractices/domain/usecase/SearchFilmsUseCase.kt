package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.model.FilmShort
import com.example.consecutivepractices.domain.repository.FilmRepository

class SearchFilmsUseCase(private val repository: FilmRepository) {
    // Принимает 3 параметра и передает их в репозиторий
    suspend operator fun invoke(query: String, type: String, year: String): List<FilmShort> {
        return repository.searchFilms(query, type, year)
    }
}