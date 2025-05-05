package com.biggie.jokeswipe.presentation.navigation

sealed class Screen(val route: String) {
    object Splash    : Screen("splash")
    object SignIn    : Screen("sign_in")
    object Joke      : Screen("joke")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")
    object Camera    : Screen("camera")
    object Settings  : Screen("settings")
    object Share     : Screen("share")
    object SignUp    : Screen("sign_up")
}
