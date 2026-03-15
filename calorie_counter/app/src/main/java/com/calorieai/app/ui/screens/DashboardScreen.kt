package com.calorieai.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.presentation.home.HomeViewModel
import com.calorieai.app.ui.components.ErrorState
import com.calorieai.app.ui.components.LoadingIndicator
import com.calorieai.app.ui.components.MealCard
import com.calorieai.app.ui.components.PrimaryButton
import com.calorieai.app.ui.components.SecondaryButton

// Inferred from design system; no Figma file in repo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToMealTypeSelect: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> LoadingIndicator(modifier = Modifier.padding(paddingValues))
            uiState.error != null -> ErrorState(
                message = uiState.error!!,
                modifier = Modifier.padding(paddingValues)
            )
            else -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Daily summary
                MealCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "0 kcal",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "No meals logged yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Meal slots (breakfast, lunch, dinner, snack)
                Text(
                    text = "Meal slots",
                    style = MaterialTheme.typography.titleMedium
                )
                listOf(
                    MealType.BREAKFAST,
                    MealType.LUNCH,
                    MealType.DINNER,
                    MealType.SNACK
                ).forEach { mealType ->
                    MealCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = mealType.name.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "—",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Progress preview
                MealCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Daily progress",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "0% of goal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Main Add Meal action
                PrimaryButton(
                    text = "Add meal",
                    onClick = onNavigateToMealTypeSelect,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Secondary navigation
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SecondaryButton(
                        text = "Meal history",
                        onClick = onNavigateToHistory,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SecondaryButton(
                        text = "Settings",
                        onClick = onNavigateToSettings,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
