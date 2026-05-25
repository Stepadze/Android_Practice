package com.example.consecutivepractices.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFilmDao {

    @Query("SELECT * FROM favorite_films ORDER BY title ASC")
    fun getAllFavorites(): Flow<List<FavoriteFilmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(film: FavoriteFilmEntity)

    @Query("DELETE FROM favorite_films WHERE imdbId = :imdbId")
    suspend fun removeFromFavorites(imdbId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE imdbId = :imdbId)")
    fun isFavorite(imdbId: String): Flow<Boolean>
}