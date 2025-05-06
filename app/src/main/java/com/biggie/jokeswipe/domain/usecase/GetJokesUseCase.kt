package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import javax.inject.Inject

/**
 * Use case to fetch a random joke.
 */
class GetJokesUseCase @Inject constructor(
    private val repository: JokeRepository
) {
    /** Delegate to repository */
    suspend operator fun invoke(): Joke =
        repository.getRandomJoke()
}