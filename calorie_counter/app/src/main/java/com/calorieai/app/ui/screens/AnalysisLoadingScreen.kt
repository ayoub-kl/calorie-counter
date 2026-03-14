package com.calorieai.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnalysisLoadingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: loading indicator in later prompts
    Text(text = "Analyzing meal…", modifier = modifier)
}
