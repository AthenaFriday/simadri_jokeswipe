package com.biggie.jokeswipe.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Splash    : Screen("splash")
    object SignIn    : Screen("signin")
    object SignUp    : Screen("signup")
    object Joke      : Screen("joke")
    object Favorites : Screen("favorites")
    object Settings  : Screen("settings")

    /**
     * Camera destination expects a single query‑param `jokeText`.
     * Usage: navController.navigate( Screen.Camera.cameraWithJoke("...") )
     */
    object Camera : Screen("camera")
    fun cameraWithJoke(jokeText: String): String =
        "$route?jokeText=${Uri.encode(jokeText)}"

    /**
     * Share destination expects two query‑params: `uri` and `jokeText`.
     * Usage: navController.navigate( Screen.Share.shareWith(uri, "…") )
     */
    object Share : Screen("share")
    fun shareWith(uri: String, jokeText: String): String =
        "$route?uri=${Uri.encode(uri)}&jokeText=${Uri.encode(jokeText)}"
}