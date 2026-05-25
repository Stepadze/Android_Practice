package com.example.practice3final.domain.repository

import com.example.practice3final.domain.model.Film

interface FilmRepository {
    suspend fun getAllFilms(): List<Film>
    suspend fun getFilmById(id: String): Film?
}