package com.example.practice3final.data.repository

import com.example.practice3final.data.api.OmdbApi
import com.example.practice3final.data.dto.FilmDetailDto
import com.example.practice3final.data.dto.FilmDto
import com.example.practice3final.data.dto.FilmSearchResponseDto
import com.example.practice3final.domain.model.Film
import com.example.practice3final.domain.repository.FilmRepository

class FilmRepositoryImpl(
    private val api: OmdbApi
) : FilmRepository {

    override suspend fun searchFilms(
        query: String,
        type: String?,
        year: String?
    ): List<Film> {
        val response: FilmSearchResponseDto = api.searchFilms(query, type, year)

        return if (response.response == "True") {
            response.films?.map { filmDto: FilmDto ->
                Film(
                    imdbId = filmDto.imdbId,
                    title = filmDto.title,
                    year = filmDto.year,
                    posterUrl = filmDto.posterUrl
                )
            } ?: emptyList()
        } else {
            emptyList()
        }
    }

    override suspend fun getFilmById(imdbId: String): Film {
        val response: FilmDetailDto = api.getFilmById(imdbId)

        return Film(
            imdbId = imdbId,
            title = response.title,
            year = response.year,
            posterUrl = response.posterUrl,
            genre = response.genre,
            director = response.director,
            description = response.plot,
            rating = response.rating,
            runtime = response.runtime,
            actors = response.actors
        )
    }
}