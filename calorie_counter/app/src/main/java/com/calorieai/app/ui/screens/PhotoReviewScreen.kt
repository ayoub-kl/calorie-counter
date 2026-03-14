package com.calorieai.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhotoReviewScreen(
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
    onRetake: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: photo preview UI in later prompts
    Text(text = "Photo Preview", modifier = modifier)
}
