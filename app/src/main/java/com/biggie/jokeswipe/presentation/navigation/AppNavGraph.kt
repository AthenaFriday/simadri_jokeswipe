package com.biggie.jokeswipe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.biggie.jokeswipe.presentation.auth.SignInScreen
import com.biggie.jokeswipe.presentation.auth.SignUpScreen
import com.biggie.jokeswipe.presentation.camera.CameraCaptureScreen
import com.biggie.jokeswipe.presentation.favorites.FavoritesScreen
import com.biggie.jokeswipe.presentation.joke.JokeScreen
import com.biggie.jokeswipe.presentation.settings.SettingsScreen
import com.biggie.jokeswipe.presentation.share.ShareScreen
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
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(Screen.Joke.route) {
            JokeScreen(navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        // CAMERA — expects a `jokeText` query param
        composable(
            route = Screen.Camera.route + "?jokeText={jokeText}",
            arguments = listOf(
                navArgument("jokeText") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val jokeText = backStackEntry
                .arguments!!
                .getString("jokeText")!!
            CameraCaptureScreen(navController, jokeText)
        }

        // SHARE — expects `uri` + `jokeText`
        composable(
            route = Screen.Share.route + "?uri={uri}&jokeText={jokeText}",
            arguments = listOf(
                navArgument("uri") {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument("jokeText") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val uriString = backStackEntry
                .arguments!!
                .getString("uri")!!
            val jokeText = backStackEntry
                .arguments!!
                .getString("jokeText")!!
            ShareScreen(navController, uriString, jokeText)
        }
    }
}
