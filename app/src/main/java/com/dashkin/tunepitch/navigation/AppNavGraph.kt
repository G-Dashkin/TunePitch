package com.dashkin.tunepitch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dashkin.tunepitch.feature.home.presentation.screen.HomeScreen

object Routes {
    const val HOME = "home"
    const val TUNER = "tuner"
    const val EXERCISE = "exercise/{exerciseId}"

    fun exercise(exerciseId: String): String = "exercise/$exerciseId"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToExercise = { exerciseId ->
//                    navController.navigate(Routes.exercise(exerciseId))
                },
                onNavigateToTuner = {
//                    navController.navigate(Routes.TUNER)
                }
            )
        }
    }
}
