package com.calorieai.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.calorieai.app.navigation.CalorieCounterNavHost

@Composable
fun CalorieCounterApp() {
    val navController = rememberNavController()
    CalorieCounterNavHost(navController = navController)
}
