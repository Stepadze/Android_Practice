package com.example.consecutivepractices.domain.model

data class Film(
    val imdbId: String,
    val title: String,
    val year: String,
    val genre: String,
    val director: String,
    val description: String,
    val rating: String,
    val posterUrl: String,
    val runtime: String,
    val actors: String
)

data class FilmShort(
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String
)