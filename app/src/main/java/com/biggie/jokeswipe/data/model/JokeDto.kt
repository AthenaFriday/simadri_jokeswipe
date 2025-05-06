package com.biggie.jokeswipe.data.model

import com.biggie.jokeswipe.domain.model.Joke
import com.google.gson.annotations.SerializedName


data class JokeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("setup") val setup: String,
    @SerializedName("punchline") val punchline: String
) {
    fun toJoke(): Joke = Joke(
        id = id,
        category = type,
        setup = setup,
        punchline = punchline
    )
}

fun List<JokeDto>.toJokeList(): List<Joke> = map { it.toJoke() }