package com.biggie.jokeswipe.data.api

import com.biggie.jokeswipe.data.model.JokeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface JokeApi {
    @GET("jokes/random")
    suspend fun getRandomJoke(): JokeDto

    @GET("jokes/ten")
    suspend fun getTenJokes(): List<JokeDto>

    @GET("jokes/{type}/random")
    suspend fun getJokeByType(@Path("type") type: String): JokeDto
}