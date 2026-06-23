package com.example.practice3final.domain.usecase

import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.repository.FilmRepository

class SearchFilmsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(
        query: String,
        type: String? = null,
        year: String? = null
    ): List<Film> {
        return repository.searchFilms(query, type, year)
    }
}