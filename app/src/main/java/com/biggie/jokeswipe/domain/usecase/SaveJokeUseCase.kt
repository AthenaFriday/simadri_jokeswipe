package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import com.biggie.jokeswipe.domain.util.Resource
import javax.inject.Inject

class SaveJokeUseCase @Inject constructor(
    private val repository: JokeRepository
) {
    suspend operator fun invoke(joke: Joke): Resource<Unit> {
        return try {
            repository.addToFavorites(joke)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not save joke")
        }
    }
}