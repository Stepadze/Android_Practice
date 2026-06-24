package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.repository.FilmRepository

class GetFilmByIdUseCase(private val repository: FilmRepository) {
    suspend operator fun invoke(imdbId: String): Film {
        return repository.getFilmById(imdbId)
    }
}