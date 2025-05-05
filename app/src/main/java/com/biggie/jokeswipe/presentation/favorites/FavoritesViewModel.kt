package com.biggie.jokeswipe.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.usecase.DeleteJokeUseCase
import com.biggie.jokeswipe.domain.usecase.GetJokesUseCase
import com.biggie.jokeswipe.domain.usecase.SaveJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getJokesUseCase: GetJokesUseCase,
    private val saveJokeUseCase: SaveJokeUseCase,
    private val deleteJokeUseCase: DeleteJokeUseCase
) : ViewModel() {
    private val _favorites = MutableStateFlow<List<Joke>>(emptyList())
    val favorites: StateFlow<List<Joke>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getJokesUseCase.getTenJokes() // placeholder for initial load
            // Ideally, repository exposes flow of favorites
        }
    }

    fun removeFromFavorites(joke: Joke) {
        viewModelScope.launch {
            deleteJokeUseCase(joke)
            loadFavorites()
        }
    }
}
