package com.example.consecutivepractices.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films")
data class FavoriteFilmEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String
)