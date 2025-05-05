package com.biggie.jokeswipe.domain.repository

import com.biggie.jokeswipe.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface JokeRepository {

    suspend fun getRandomJoke(): Joke

    suspend fun getTenJokes(): List<Joke>

    suspend fun getJokeByType(type: String): Joke

    fun getFavoriteJokes(): Flow<List<Joke>>

    suspend fun addToFavorites(joke: Joke)

    suspend fun removeFromFavorites(joke: Joke)
}