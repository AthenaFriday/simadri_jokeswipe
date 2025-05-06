package com.biggie.jokeswipe.presentation.joke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.usecase.GetJokesUseCase
import com.biggie.jokeswipe.domain.usecase.SaveJokeUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokeViewModel @Inject constructor(
    private val getJokesUseCase: GetJokesUseCase,
    private val saveJokeUseCase: SaveJokeUseCase
) : ViewModel() {

    private val _joke = MutableStateFlow<Joke?>(null)
    val joke: StateFlow<Joke?> = _joke

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadRandomJoke()
    }

    /** Fetch a random joke and update UI state */
    fun loadRandomJoke() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _joke.value = getJokesUseCase()       // resolves now!
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to load joke"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Save the current joke to this user’s favorites,
     * then load another.
     */
    fun saveFavorite() {
        val current = _joke.value ?: return
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            saveJokeUseCase(uid, current)           // resolves now!
            loadRandomJoke()
        }
    }
}