package com.calorieai.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.calorieai.app.ui.components.PrimaryButton
import com.calorieai.app.ui.components.SecondaryButton

@Composable
fun AnalysisLoadingScreen(
    onNavigateBack: () -> Unit,
    onAnalysisComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: loading indicator in later prompts; complete action wires flow to MEAL_RESULT
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Analyzing meal…")
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(text = "Continue", onClick = onAnalysisComplete)
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryButton(text = "Back", onClick = onNavigateBack)
    }
}
