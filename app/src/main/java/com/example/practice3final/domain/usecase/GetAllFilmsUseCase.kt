package com.example.practice3final.domain.usecase

import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.repository.FilmRepository

class GetAllFilmsUseCase(
    private val repository: FilmRepository
) {
    suspend operator fun invoke(): List<Film> {
        return repository.getAllFilms()
    }
}