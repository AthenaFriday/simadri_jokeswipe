package com.biggie.jokeswipe.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM favorite_jokes")
    fun getAllFavorites(): Flow<List<JokeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: JokeEntity)

    @Delete
    suspend fun delete(joke: JokeEntity)
}
