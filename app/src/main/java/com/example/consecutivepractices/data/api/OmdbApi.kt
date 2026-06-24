package com.example.consecutivepractices.data.api

import com.example.consecutivepractices.data.dto.FilmDetailDto
import com.example.consecutivepractices.data.dto.FilmSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/?apikey=74585e2d")
    suspend fun searchFilms(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("y") year: String = ""
    ): FilmSearchResponseDto

    @GET("/?apikey=74585e2d")
    suspend fun getFilmById(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): FilmDetailDto

    companion object {
        const val BASE_URL = "https://www.omdbapi.com"
    }
}
