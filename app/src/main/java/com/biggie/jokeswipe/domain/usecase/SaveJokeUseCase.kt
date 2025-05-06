package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import javax.inject.Inject

/**
 * Use case to save a joke into the current user's favorites.
 */
class SaveJokeUseCase @Inject constructor(
    private val repository: JokeRepository
) {
    /** Delegate to repository */
    suspend operator fun invoke(userId: String, joke: Joke) =
        repository.addToFavorites(userId, joke)
}