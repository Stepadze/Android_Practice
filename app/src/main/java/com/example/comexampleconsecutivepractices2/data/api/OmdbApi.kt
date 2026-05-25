package com.example.consecutivepractices.data.api

import com.example.consecutivepractices.data.dto.FilmSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {
    @GET("/")
    suspend fun searchFilms(
        @Query("s") query: String,
        @Query("type") type: String? = null,
        @Query("y") year: String? = null,
        @Query("apikey") apiKey: String = "74585e2d"
    ): FilmSearchResponse
}