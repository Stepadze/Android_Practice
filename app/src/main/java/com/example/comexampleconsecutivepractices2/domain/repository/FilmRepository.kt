package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.Film

interface FilmRepository {
    suspend fun searchFilms(query: String, type: String?, year: String?): List<Film>
}