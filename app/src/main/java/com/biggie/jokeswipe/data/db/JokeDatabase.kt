package com.biggie.jokeswipe.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [JokeEntity::class],
    version = 2,
    exportSchema = false
)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun jokeDao(): JokeDao
}
