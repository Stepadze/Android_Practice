package com.example.consecutivepractices.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Query("SELECT * FROM favorite_films")
    fun getAllFavorites(): Flow<List<FavoriteFilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(film: FavoriteFilmEntity)

    @Delete
    suspend fun removeFromFavorites(film: FavoriteFilmEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE imdbId = :imdbId)")
    fun isFavorite(imdbId: String): Flow<Boolean>
}