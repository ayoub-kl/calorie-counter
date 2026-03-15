package com.calorieai.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.presentation.mealresult.MealResultViewModel
import com.calorieai.app.ui.components.ErrorState
import com.calorieai.app.ui.components.FoodRow
import com.calorieai.app.ui.components.NutritionSummary
import com.calorieai.app.ui.components.PrimaryButton
import com.calorieai.app.ui.components.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealResultScreen(
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    onEdit: () -> Unit,
    viewModel: MealResultViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect { onSave() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(mealTypeTitle(uiState.mealType)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.error != null -> ErrorState(
                message = uiState.error!!,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
            )
            uiState.isEmpty -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No foods detected",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                SecondaryButton(text = "Add food", onClick = { viewModel.addFood() })
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Save meal", onClick = { viewModel.saveMeal() }, enabled = !isSaving)
            }
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(uiState.foods, key = { it.id }) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FoodRow(
                                name = item.name,
                                quantityAndUnit = "${formatQuantity(item.quantity)} ${item.unit}",
                                calories = "${item.calories.toInt()} kcal",
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.removeFood(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                }
                NutritionSummary(
                    calories = uiState.totals.calories,
                    proteinG = uiState.totals.proteinG,
                    carbsG = uiState.totals.carbsG,
                    fatG = uiState.totals.fatG,
                    modifier = Modifier.fillMaxWidth()
                )
                uiState.overallConfidence?.let { conf ->
                    Text(
                        text = "Confidence: ${(conf * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(text = "Edit", onClick = onEdit)
                Spacer(modifier = Modifier.height(8.dp))
                SecondaryButton(text = "Add food", onClick = { viewModel.addFood() })
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(text = "Save meal", onClick = { viewModel.saveMeal() }, enabled = !isSaving)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun mealTypeTitle(mealType: MealType): String = when (mealType) {
    MealType.BREAKFAST -> "Breakfast"
    MealType.LUNCH -> "Lunch"
    MealType.DINNER -> "Dinner"
    MealType.SNACK -> "Snack"
}

private fun formatQuantity(q: Double): String = when {
    q == q.toLong().toDouble() -> q.toLong().toString()
    else -> "%.1f".format(q)
}
