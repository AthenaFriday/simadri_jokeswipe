package com.biggie.jokeswipe.data.repository

import com.biggie.jokeswipe.data.api.JokeApi
import com.biggie.jokeswipe.data.db.JokeDao
import com.biggie.jokeswipe.data.db.JokeEntity
import com.biggie.jokeswipe.data.db.toEntity
import com.biggie.jokeswipe.data.model.JokeDto
import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JokeRepositoryImpl @Inject constructor(
    private val api: JokeApi,
    private val dao: JokeDao
) : JokeRepository {

    override suspend fun getRandomJoke(): Joke =
        api.getRandomJoke().toJoke()

    override suspend fun getTenJokes(): List<Joke> =
        api.getTenJokes().map { it.toJoke() }

    override suspend fun getJokeByType(type: String): Joke =
        api.getJokeByType(type).toJoke()

    override fun getFavoriteJokes(): Flow<List<Joke>> =
        dao.getAllFavorites().map { list -> list.map { it.toJoke() } }

    override suspend fun addToFavorites(joke: Joke) {
        dao.insert(joke.toEntity())
    }

    override suspend fun removeFromFavorites(joke: Joke) {
        dao.delete(joke.toEntity())
    }
}
