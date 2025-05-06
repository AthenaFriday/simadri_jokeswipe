package com.biggie.jokeswipe.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.biggie.jokeswipe.domain.model.Joke
import com.biggie.jokeswipe.domain.repository.JokeRepository
import com.biggie.jokeswipe.domain.usecase.DeleteJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: JokeRepository,
    private val deleteJokeUseCase: DeleteJokeUseCase
) : ViewModel() {

    private val userId: String? get() = FirebaseAuth.getInstance().currentUser?.uid

    private val favoritesFlow: Flow<List<Joke>> = userId
        ?.let { repository.getFavoriteJokes(it) }
        ?: flowOf(emptyList())

    val favorites: StateFlow<List<Joke>> = favoritesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun removeFromFavorites(joke: Joke) {
        userId?.let { uid ->
            viewModelScope.launch { deleteJokeUseCase(uid, joke) }
        }
    }
}