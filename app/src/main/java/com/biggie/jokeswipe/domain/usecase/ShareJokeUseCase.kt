package com.biggie.jokeswipe.domain.usecase

import com.biggie.jokeswipe.domain.model.Joke
import javax.inject.Inject

class ShareJokeUseCase @Inject constructor() {
    operator fun invoke(joke: Joke): Joke = joke
}