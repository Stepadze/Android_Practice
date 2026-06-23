package com.example.practice3final.domain.repository

import com.example.practice3final.domain.model.Film

interface FilmRepository {
    suspend fun searchFilms(
        query: String,
        type: String? = null,
        year: String? = null
    ): List<Film>

    suspend fun getFilmById(imdbId: String): Film
}