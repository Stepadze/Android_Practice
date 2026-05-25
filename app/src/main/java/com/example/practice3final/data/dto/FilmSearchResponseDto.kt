package com.example.practice3final.data.dto

import com.google.gson.annotations.SerializedName

data class FilmSearchResponseDto(
    @SerializedName("Search") val films: List<FilmShortDto>?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)

data class FilmShortDto(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbId: String,
    @SerializedName("Poster") val posterUrl: String
)