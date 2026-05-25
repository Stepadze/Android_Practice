package com.example.practice3final.data.api

import com.example.practice3final.data.dto.FilmSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchFilms(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = "74585e2d"
    ): FilmSearchResponseDto
}