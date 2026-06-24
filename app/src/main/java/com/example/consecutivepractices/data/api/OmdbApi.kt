package com.example.consecutivepractices.data.api

import com.example.consecutivepractices.data.dto.FilmDetailDto
import com.example.consecutivepractices.data.dto.FilmSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    // Поиск фильмов по названию (query параметр)
    @GET("/")
    suspend fun searchFilms(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("apikey") apiKey: String = API_KEY
    ): FilmSearchResponseDto

    // Детали фильма по ID (path-style query параметр)
    @GET("/")
    suspend fun getFilmById(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = API_KEY
    ): FilmDetailDto

    companion object {
        const val BASE_URL = "https://www.omdbapi.com"
        const val API_KEY = "74585e2d"
    }
}