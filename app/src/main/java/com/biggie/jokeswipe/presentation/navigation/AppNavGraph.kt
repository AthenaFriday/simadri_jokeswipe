package com.biggie.jokeswipe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.biggie.jokeswipe.presentation.auth.SignInScreen
import com.biggie.jokeswipe.presentation.auth.SignUpScreen
//import com.biggie.jokeswipe.presentation.camera.CameraCaptureScreen
import com.biggie.jokeswipe.presentation.favorites.FavoritesScreen
import com.biggie.jokeswipe.presentation.joke.JokeScreen
//import com.biggie.jokeswipe.presentation.profile.ProfileScreen
import com.biggie.jokeswipe.presentation.settings.SettingsScreen
//import com.biggie.jokeswipe.presentation.share.ShareScreen
import com.biggie.jokeswipe.presentation.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.SignIn.route) {
            SignInScreen(navController)
        }
        composable(Screen.Joke.route) {
            JokeScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable(Screen.Profile.route) {
//            ProfileScreen(navController)
        }
        composable(Screen.Camera.route) {
//            CameraCaptureScreen(navController, jokeText = "YourDefaultJokeText")
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(
            route = "${Screen.Share.route}/{jokeText}",
            arguments = listOf(navArgument("jokeText") { type = NavType.StringType })
        ) { backStackEntry ->
            val jokeText = backStackEntry.arguments?.getString("jokeText") ?: ""
//            ShareScreen(navController, jokeText)
        }
    }
}
