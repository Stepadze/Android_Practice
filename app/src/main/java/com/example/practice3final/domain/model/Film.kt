package com.example.practice3final.domain.model

data class Film(
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val genre: String = "",
    val director: String = "",
    val description: String = "",
    val rating: String = "",
    val runtime: String = "",
    val actors: String = ""
)