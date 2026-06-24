package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.model.FilmShort

interface FilmRepository {
    suspend fun searchFilms(query: String): List<FilmShort>
    suspend fun getFilmById(imdbId: String): Film
}