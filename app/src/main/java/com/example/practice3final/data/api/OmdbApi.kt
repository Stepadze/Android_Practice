package com.example.practice3final.data.api

import com.example.practice3final.data.dto.FilmDetailDto
import com.example.practice3final.data.dto.FilmSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchFilms(
        @Query("s") query: String,
        @Query("type") type: String? = null,
        @Query("y") year: String? = null,
        @Query("apikey") apiKey: String = "74585e2d"
    ): FilmSearchResponseDto

    @GET("/")
    suspend fun getFilmById(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full",
        @Query("apikey") apiKey: String = "74585e2d"
    ): FilmDetailDto
}