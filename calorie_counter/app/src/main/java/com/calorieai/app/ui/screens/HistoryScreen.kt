package com.calorieai.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onMealSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: meal history list in later prompts
    Text(text = "Meal History", modifier = modifier)
}
