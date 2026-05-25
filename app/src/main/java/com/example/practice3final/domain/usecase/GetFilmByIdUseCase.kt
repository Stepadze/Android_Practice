package com.example.practice3final.domain.usecase

import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.repository.FilmRepository

class GetFilmByIdUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(id: String): Film? {
        return repository.getFilmById(id)
    }
}