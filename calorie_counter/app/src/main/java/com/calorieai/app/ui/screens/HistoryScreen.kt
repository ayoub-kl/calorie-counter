package com.calorieai.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorieai.app.domain.model.Meal
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.presentation.history.HistoryViewModel
import com.calorieai.app.ui.components.ErrorState
import com.calorieai.app.ui.components.LoadingIndicator
import com.calorieai.app.ui.components.MealCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onMealSelected: (String) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("History") },
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
                message = uiState.error!!.userMessage,
                onRetry = if (uiState.error!!.recoverable) ({ viewModel.retry() }) else null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            )
            uiState.meals.isEmpty() -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No meals yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(uiState.meals, key = { it.id }) { meal ->
                    MealCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMealSelected(meal.id) }
                    ) {
                        HistoryMealRow(meal = meal)
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun HistoryMealRow(meal: Meal) {
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
    val timeText = dateFormat.format(Date(meal.createdAt))
    Column {
        Text(
            text = mealTypeLabel(meal.mealType),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${meal.totalCalories.toInt()} kcal",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = timeText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun mealTypeLabel(mealType: MealType): String = when (mealType) {
    MealType.BREAKFAST -> "Breakfast"
    MealType.LUNCH -> "Lunch"
    MealType.DINNER -> "Dinner"
    MealType.SNACK -> "Snack"
}
