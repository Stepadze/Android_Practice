package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.api.OmdbApi
import com.example.consecutivepractices.domain.model.Film
import com.example.consecutivepractices.domain.model.FilmShort
import com.example.consecutivepractices.domain.repository.FilmRepository

class FilmRepositoryImpl(private val api: OmdbApi) : FilmRepository {

    override suspend fun searchFilms(query: String): List<FilmShort> {
        val response = api.searchFilms(query)
        if (response.response == "False") {
            throw Exception(response.error ?: "Ничего не найдено")
        }
        return response.films?.map { dto ->
            FilmShort(
                imdbId = dto.imdbId,
                title = dto.title,
                year = dto.year,
                posterUrl = dto.posterUrl
            )
        } ?: emptyList()
    }

    override suspend fun getFilmById(imdbId: String): Film {
        val dto = api.getFilmById(imdbId)
        if (dto.response == "False") {
            throw Exception(dto.error ?: "Фильм не найден")
        }
        return Film(
            imdbId = imdbId,
            title = dto.title,
            year = dto.year,
            genre = dto.genre,
            director = dto.director,
            description = dto.plot,
            rating = dto.rating,
            posterUrl = dto.posterUrl,
            runtime = dto.runtime,
            actors = dto.actors
        )
    }
}