package com.biggie.jokeswipe.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.biggie.jokeswipe.domain.model.Joke

@Entity(tableName = "favorite_jokes")
data class JokeEntity(
    @PrimaryKey val id: Int,
    val userId: String,
    val category: String,
    val setup: String,
    val punchline: String
) {
    /** Map entity → domain */
    fun toDomain(): Joke = Joke(
        id = id,
        category = category,
        setup = setup,
        punchline = punchline
    )

    companion object {
        /** Map domain → entity */
        fun fromDomain(userId: String, joke: Joke): JokeEntity =
            JokeEntity(
                id = joke.id,
                userId = userId,
                category = joke.category,
                setup = joke.setup,
                punchline = joke.punchline
            )
    }
}