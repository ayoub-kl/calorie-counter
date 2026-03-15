package com.calorieai.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.presentation.mealdetail.MealDetailViewModel
import com.calorieai.app.ui.components.ErrorState
import com.calorieai.app.ui.components.FoodRow
import com.calorieai.app.ui.components.LoadingIndicator
import com.calorieai.app.ui.components.NutritionSummary
import com.calorieai.app.ui.components.SecondaryButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit,
    viewModel: MealDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Meal detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            uiState.error != null -> ErrorState(
                message = uiState.error!!,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            )
            uiState.meal == null -> ErrorState(
                message = "Meal not found",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            )
            else -> {
                val meal = uiState.meal!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mealTypeLabel(meal.mealType),
                        style = MaterialTheme.typography.titleLarge
                    )
                    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                    Text(
                        text = dateFormat.format(Date(meal.createdAt)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Foods",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    meal.foodItems.forEach { item ->
                        FoodRow(
                            name = item.name,
                            quantityAndUnit = "${formatQuantity(item.quantity)} ${item.unit}",
                            calories = "${item.calories.toInt()} kcal",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    NutritionSummary(
                        calories = meal.totalCalories,
                        proteinG = meal.totalProteinG,
                        carbsG = meal.totalCarbsG,
                        fatG = meal.totalFatG,
                        modifier = Modifier.fillMaxWidth()
                    )
                    meal.overallConfidence?.let { conf ->
                        Text(
                            text = "Confidence: ${(conf * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SecondaryButton(text = "Edit", onClick = onEdit, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

private fun mealTypeLabel(mealType: MealType): String = when (mealType) {
    MealType.BREAKFAST -> "Breakfast"
    MealType.LUNCH -> "Lunch"
    MealType.DINNER -> "Dinner"
    MealType.SNACK -> "Snack"
}

private fun formatQuantity(q: Double): String = when {
    q == q.toLong().toDouble() -> q.toLong().toString()
    else -> "%.1f".format(q)
}
