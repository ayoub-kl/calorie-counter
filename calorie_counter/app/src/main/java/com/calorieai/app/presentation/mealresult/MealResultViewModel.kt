package com.calorieai.app.presentation.mealresult

import androidx.lifecycle.ViewModel
import com.calorieai.app.data.AnalysisResultHolder
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.domain.model.NutritionTotals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MealResultViewModel @Inject constructor(
    private val resultHolder: AnalysisResultHolder
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealResultUiState())
    val uiState: StateFlow<MealResultUiState> = _uiState.asStateFlow()

    init {
        loadFromHolder()
    }

    private fun loadFromHolder() {
        val result = resultHolder.lastResult
        if (result == null) {
            _uiState.value = MealResultUiState(error = "No meal result")
            return
        }
        _uiState.value = MealResultUiState(
            mealType = result.mealType,
            foods = result.foods.toMutableList(),
            totals = result.totals,
            overallConfidence = result.overallConfidence,
            userEdited = result.userEdited,
            error = null
        )
    }

    fun removeFood(id: String) {
        val current = _uiState.value
        val newFoods = current.foods.filter { it.id != id }
        if (newFoods == current.foods) return
        _uiState.value = current.copy(
            foods = newFoods,
            totals = computeTotals(newFoods),
            userEdited = true,
            error = null
        )
    }

    fun addFood() {
        val current = _uiState.value
        val newItem = FoodItem(
            id = UUID.randomUUID().toString(),
            name = "New item",
            quantity = 1.0,
            unit = "serving",
            calories = 0.0,
            proteinG = 0.0,
            carbsG = 0.0,
            fatG = 0.0,
            confidence = null
        )
        val newFoods = current.foods + newItem
        _uiState.value = current.copy(
            foods = newFoods,
            totals = computeTotals(newFoods),
            userEdited = true,
            error = null
        )
    }

    private fun computeTotals(foods: List<FoodItem>): NutritionTotals = NutritionTotals(
        calories = foods.sumOf { it.calories },
        proteinG = foods.sumOf { it.proteinG },
        carbsG = foods.sumOf { it.carbsG },
        fatG = foods.sumOf { it.fatG }
    )
}

data class MealResultUiState(
    val mealType: MealType = MealType.LUNCH,
    val foods: List<FoodItem> = emptyList(),
    val totals: NutritionTotals = NutritionTotals(0.0, 0.0, 0.0, 0.0),
    val overallConfidence: Float? = null,
    val userEdited: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasContent: Boolean = error == null && foods.isNotEmpty()
    val isEmpty: Boolean = error == null && foods.isEmpty()
}
