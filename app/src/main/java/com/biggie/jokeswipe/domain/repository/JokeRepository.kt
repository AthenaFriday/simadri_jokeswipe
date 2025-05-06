package com.biggie.jokeswipe.domain.repository

import com.biggie.jokeswipe.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface JokeRepository {
    suspend fun getRandomJoke(): Joke
    suspend fun getTenJokes(): List<Joke>
    suspend fun getJokeByType(type: String): Joke

    fun getFavoriteJokes(userId: String): Flow<List<Joke>>
    suspend fun addToFavorites(userId: String, joke: Joke)
    suspend fun removeFromFavorites(userId: String, joke: Joke)
}