package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import javax.inject.Inject

class GetJokesUseCase @Inject constructor(
    private val repository: JokeRepository
) {
    suspend fun getRandomJoke(): Joke = repository.getRandomJoke()
    suspend fun getTenJokes(): List<Joke> = repository.getTenJokes()
    suspend fun getJokeByType(type: String): Joke = repository.getJokeByType(type)
}
