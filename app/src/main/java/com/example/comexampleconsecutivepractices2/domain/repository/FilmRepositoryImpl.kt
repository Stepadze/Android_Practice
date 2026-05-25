package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.api.OmdbApi
import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.repository.FilmRepository

class FilmRepositoryImpl(private val api: OmdbApi) : FilmRepository {
    override suspend fun searchFilms(query: String, type: String?, year: String?): List<Film> {
        val response = api.searchFilms(query, type, year)
        return if (response.response == "True") {
            response.films?.map { dto ->
                Film(dto.imdbId, dto.title, dto.year, dto.posterUrl)
            } ?: emptyList()
        } else {
            emptyList()
        }
    }
}