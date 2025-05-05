package com.biggie.jokeswipe.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.biggie.jokeswipe.domain.model.Joke

@Entity(tableName = "favorite_jokes")
data class JokeEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val setup: String,
    val punchline: String
) {
    fun toJoke(): Joke = Joke(
        id = id,
        category = category,
        setup = setup,
        punchline = punchline
    )
}

fun Joke.toEntity(): JokeEntity = JokeEntity(
    id = id,
    category = category,
    setup = setup,
    punchline = punchline
)