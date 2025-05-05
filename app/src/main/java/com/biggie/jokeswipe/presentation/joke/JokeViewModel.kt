package com.biggie.jokeswipe.presentation.joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.usecase.GetJokesUseCase
import com.biggie.jokeswipe.domain.usecase.SaveJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for JokeScreen, handling joke loading and saving favorites.
 */
@HiltViewModel
class JokeViewModel @Inject constructor(
    private val getJokesUseCase: GetJokesUseCase,
    private val saveJokeUseCase: SaveJokeUseCase
) : ViewModel() {

    // Holds the current joke
    private val _joke = MutableStateFlow<Joke?>(null)
    val joke: StateFlow<Joke?> = _joke

    // Loading indicator state
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Error message state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Load a joke initially
        loadRandomJoke()
    }

    /**
     * Loads a random joke and updates loading/error states.
     */
    fun loadRandomJoke() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = getJokesUseCase.getRandomJoke()
                _joke.value = result
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to load joke"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Saves the provided joke to favorites, then optionally loads a new one.
     */
    fun saveFavorite(joke: Joke) {
        viewModelScope.launch {
            try {
                saveJokeUseCase(joke)
            } catch (e: Exception) {
                // Optionally handle save error
            }
        }
    }
}