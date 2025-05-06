package com.biggie.jokeswipe.presentation.navigation

sealed class Screen(val route: String) {
    object Splash   : Screen("splash")
    object SignIn   : Screen("sign_in")
    object SignUp   : Screen("sign_up")
    object Joke     : Screen("joke")
    object Favorites: Screen("favorites")
    object Settings : Screen("settings")
    object Camera   : Screen("camera")
    object Share    : Screen("share")
}
