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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.presentation.mealresult.MealResultViewModel
import com.calorieai.app.ui.components.NutritionSummary
import com.calorieai.app.ui.components.PrimaryButton
import com.calorieai.app.ui.components.SecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealEditScreen(
    onNavigateBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: MealResultViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Edit meal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.foods, key = { it.id }) { item ->
                    EditFoodRow(
                        item = item,
                        onQuantityChange = { q -> viewModel.updateFood(item.id, q, item.unit) },
                        onUnitChange = { u -> viewModel.updateFood(item.id, item.quantity, u) },
                        onRemove = { viewModel.removeFood(item.id) }
                    )
                }
            }
            NutritionSummary(
                calories = uiState.totals.calories,
                proteinG = uiState.totals.proteinG,
                carbsG = uiState.totals.carbsG,
                fatG = uiState.totals.fatG,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Add food", onClick = { viewModel.addFood() })
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Save", onClick = onSave)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EditFoodRow(
    item: FoodItem,
    onQuantityChange: (Double) -> Unit,
    onUnitChange: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = formatQuantityForEdit(item.quantity),
                    onValueChange = { onQuantityChange(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Qty") },
                    modifier = Modifier.fillMaxWidth(0.35f)
                )
                OutlinedTextField(
                    value = item.unit,
                    onValueChange = onUnitChange,
                    label = { Text("Unit") },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Delete, contentDescription = "Remove")
        }
    }
}

private fun formatQuantityForEdit(q: Double): String = when {
    q == q.toLong().toDouble() -> q.toLong().toString()
    else -> "%.2f".format(q)
}
