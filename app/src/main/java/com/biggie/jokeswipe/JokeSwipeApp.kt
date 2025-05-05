package com.biggie.jokeswipe

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.biggie.jokeswipe.presentation.navigation.AppNavGraph
import com.biggie.jokeswipe.ui.theme.JokeSwipeTheme

@Composable
fun JokeSwipeApp() {
    JokeSwipeTheme {
        Surface(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavGraph()
        }
    }
}