package com.biggie.jokeswipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.biggie.jokeswipe.ui.theme.JokeSwipeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokeSwipeTheme {
                Surface {
                    JokeSwipeApp()
                }
            }
        }
    }
}
