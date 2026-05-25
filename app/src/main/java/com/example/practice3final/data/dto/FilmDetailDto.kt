package com.example.practice3final.data.dto

import com.google.gson.annotations.SerializedName

data class FilmDetailDto(
    @SerializedName("imdbID") val imdbId: String,
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Plot") val plot: String,
    @SerializedName("imdbRating") val rating: String,
    @SerializedName("Poster") val posterUrl: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Actors") val actors: String,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)