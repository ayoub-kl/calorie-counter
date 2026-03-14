package com.calorieai.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MealDetailScreen(
    mealId: String,
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: meal detail UI in later prompts
    Text(text = "Meal Detail: $mealId", modifier = modifier)
}
