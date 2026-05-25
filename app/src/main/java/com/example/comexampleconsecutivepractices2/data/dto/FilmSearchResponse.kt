package com.example.consecutivepractices.data.dto

import com.google.gson.annotations.SerializedName

data class FilmSearchResponse(
    @SerializedName("Search") val films: List<FilmDto>?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)

data class FilmDto(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbId: String,
    @SerializedName("Poster") val posterUrl: String
)