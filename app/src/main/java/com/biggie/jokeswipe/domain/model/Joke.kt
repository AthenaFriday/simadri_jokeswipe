package com.biggie.jokeswipe.domain.model

data class Joke(
    val id: Int,
    val category: String,
    val setup: String,
    val punchline: String
)