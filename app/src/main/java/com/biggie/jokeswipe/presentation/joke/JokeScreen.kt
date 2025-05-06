package com.biggie.jokeswipe.presentation.joke

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.biggie.jokeswipe.R
import com.biggie.jokeswipe.presentation.auth.AuthViewModel
import com.biggie.jokeswipe.presentation.navigation.Screen

/**
 * Main Joke screen with logout, favorites, settings, and in-card actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JokeScreen(
    navController: NavController,
    viewModel: JokeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val joke by viewModel.joke.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Load a joke once on composition
    LaunchedEffect(Unit) {
        viewModel.loadRandomJoke()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JokeSwipe") },
                navigationIcon = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                        navController.navigate(Screen.SignIn.route) {
                            popUpTo(Screen.Joke.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Favorites.route)
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.Settings.route)
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    loading -> CircularProgressIndicator()
                    error != null -> Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                    joke != null -> Text(
                        text = "${joke!!.setup}\n\n${joke!!.punchline}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { viewModel.loadRandomJoke() }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Skip",
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(onClick = {
                    viewModel.saveFavorite()
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JokeScreenPreview() {
    JokeScreen(navController = rememberNavController())
}
