package com.biggie.jokeswipe.data.repository

import com.biggie.jokeswipe.data.api.JokeApi
import com.biggie.jokeswipe.data.db.JokeDao
import com.biggie.jokeswipe.data.db.JokeEntity
import com.biggie.jokeswipe.data.model.toJokeList
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
        api.getTenJokes().toJokeList()

    override suspend fun getJokeByType(type: String): Joke =
        api.getJokeByType(type).toJoke()

    override fun getFavoriteJokes(userId: String): Flow<List<Joke>> =
        dao.getFavoritesForUser(userId).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun addToFavorites(userId: String, joke: Joke) {
        dao.insertFavorite(JokeEntity.fromDomain(userId, joke))
    }

    override suspend fun removeFromFavorites(userId: String, joke: Joke) {
        dao.deleteFavorite(JokeEntity.fromDomain(userId, joke))
    }
}
