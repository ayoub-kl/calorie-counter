package com.calorieai.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calorieai.app.ui.screens.HomeScreen

@Composable
fun CalorieCounterNavHost(navController: androidx.navigation.NavController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToMealTypeSelect = { navController.navigate(NavRoutes.MEAL_TYPE_SELECT) },
                onNavigateToHistory = { navController.navigate(NavRoutes.HISTORY) },
                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
            )
        }
        // Other routes will be wired as screens are implemented (Prompt 1: skeleton only, no UI)
    }
}
