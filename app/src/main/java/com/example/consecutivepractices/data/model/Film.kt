package com.example.consecutivepractices.data.model

data class Film(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: Float,
    val director: String,
    val description: String,
    val posterUrl: String = ""
)