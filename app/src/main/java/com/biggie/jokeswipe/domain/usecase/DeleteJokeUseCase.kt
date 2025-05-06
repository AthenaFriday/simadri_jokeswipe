package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import javax.inject.Inject

class DeleteJokeUseCase @Inject constructor(
    private val repository: JokeRepository
) {

    suspend operator fun invoke(userId: String, joke: Joke) {
        repository.removeFromFavorites(userId, joke)
    }
}