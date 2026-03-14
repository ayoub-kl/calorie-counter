package com.calorieai.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NutritionSummary(
    calories: Double,
    proteinG: Double,
    carbsG: Double,
    fatG: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Total: ${calories.toInt()} kcal",
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "P: ${proteinG.toInt()}g", style = MaterialTheme.typography.bodyMedium)
            Text(text = "C: ${carbsG.toInt()}g", style = MaterialTheme.typography.bodyMedium)
            Text(text = "F: ${fatG.toInt()}g", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
