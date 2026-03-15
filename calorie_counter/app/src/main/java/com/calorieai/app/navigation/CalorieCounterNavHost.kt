package com.calorieai.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.util.Base64
import com.calorieai.app.ui.screens.AnalysisLoadingScreen
import com.calorieai.app.ui.screens.CameraCaptureScreen
import com.calorieai.app.ui.screens.DashboardScreen
import com.calorieai.app.ui.screens.HistoryScreen
import com.calorieai.app.ui.screens.MealDetailScreen
import com.calorieai.app.ui.screens.MealEditScreen
import com.calorieai.app.ui.screens.MealResultScreen
import com.calorieai.app.ui.screens.MealTypeSelectScreen
import com.calorieai.app.ui.screens.PhotoReviewScreen
import com.calorieai.app.ui.screens.SettingsScreen

@Composable
fun CalorieCounterNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.MEAL_TYPE_SELECT
    ) {
        composable(NavRoutes.HOME) {
            DashboardScreen(
                onNavigateToMealTypeSelect = { navController.navigate(NavRoutes.MEAL_TYPE_SELECT) },
                onNavigateToHistory = { navController.navigate(NavRoutes.HISTORY) },
                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
            )
        }

        composable(NavRoutes.MEAL_TYPE_SELECT) {
            MealTypeSelectScreen(
                onNavigateBack = { navController.navigateUp() },
                onMealTypeSelected = { navController.navigate(NavRoutes.CAMERA_CAPTURE) }
            )
        }

        composable(NavRoutes.CAMERA_CAPTURE) {
            CameraCaptureScreen(
                onNavigateBack = { navController.navigateUp() },
                onPhotoCaptured = { uri -> navController.navigate(NavRoutes.photoReviewWithImage(uri)) }
            )
        }

        composable(
            route = "${NavRoutes.PHOTO_REVIEW}/{imageUri}",
            arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("imageUri") ?: ""
            val imageUri = try {
                String(Base64.getUrlDecoder().decode(encoded), java.nio.charset.StandardCharsets.UTF_8)
            } catch (_: Exception) {
                ""
            }
            PhotoReviewScreen(
                imageUri = imageUri,
                onNavigateBack = { navController.navigateUp() },
                onConfirm = { navController.navigate(NavRoutes.ANALYSIS_LOADING) },
                onRetake = { navController.navigateUp() }
            )
        }

        composable(NavRoutes.ANALYSIS_LOADING) {
            AnalysisLoadingScreen(
                onNavigateBack = { navController.navigateUp() },
                onAnalysisComplete = {
                    navController.navigate(NavRoutes.MEAL_RESULT) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoutes.MEAL_RESULT) {
            MealResultScreen(
                onNavigateBack = { navController.navigateUp() },
                onSave = { navController.navigate(NavRoutes.MEAL_TYPE_SELECT) { popUpTo(NavRoutes.MEAL_TYPE_SELECT) { inclusive = true } } },
                onEdit = { navController.navigate(NavRoutes.MEAL_EDIT) }
            )
        }

        composable(NavRoutes.MEAL_EDIT) {
            MealEditScreen(
                onNavigateBack = { navController.navigateUp() },
                onSave = { navController.navigateUp() }
            )
        }

        composable(NavRoutes.HISTORY) {
            HistoryScreen(
                onNavigateBack = { navController.navigateUp() },
                onMealSelected = { mealId -> navController.navigate(NavRoutes.mealDetail(mealId)) }
            )
        }

        composable(
            route = "${NavRoutes.MEAL_DETAIL}/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: ""
            MealDetailScreen(
                mealId = mealId,
                onNavigateBack = { navController.navigateUp() },
                onEdit = { navController.navigate(NavRoutes.MEAL_EDIT) }
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
