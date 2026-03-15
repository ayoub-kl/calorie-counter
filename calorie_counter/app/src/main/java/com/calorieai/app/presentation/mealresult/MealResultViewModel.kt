package com.calorieai.app.presentation.mealresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorieai.app.data.AnalysisResultHolder
import com.calorieai.app.domain.model.AppError
import com.calorieai.app.domain.model.AnalysisStatus
import com.calorieai.app.domain.model.FoodItem
import com.calorieai.app.domain.model.Meal
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.domain.model.NutritionTotals
import com.calorieai.app.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MealResultViewModel @Inject constructor(
    private val resultHolder: AnalysisResultHolder,
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealResultUiState())
    val uiState: StateFlow<MealResultUiState> = _uiState.asStateFlow()

    init {
        loadFromHolder()
    }

    private fun loadFromHolder() {
        val result = resultHolder.lastResult
        if (result == null) {
            _uiState.value = MealResultUiState(error = AppError.MissingResult)
            return
        }
        _uiState.value = MealResultUiState(
            id = result.analysisId,
            mealType = result.mealType,
            imageUri = result.imageUri,
            createdAt = result.captureTimestamp,
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun updateFood(id: String, quantity: Double, unit: String) {
        val current = _uiState.value
        val newFoods = current.foods.map { item ->
            if (item.id == id) item.copy(
                quantity = quantity.coerceAtLeast(0.0),
                unit = unit.ifBlank { "serving" }
            ) else item
        }
        if (newFoods == current.foods) return
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

    private val _saveSuccess = Channel<Unit>(Channel.CONFLATED)
    val saveSuccess = _saveSuccess.receiveAsFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun saveMeal() {
        if (_isSaving.value) return
        val state = _uiState.value
        if (state.id.isBlank() || state.foods.isEmpty()) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                val meal = Meal(
                    id = state.id,
                    mealType = state.mealType,
                    imageUri = state.imageUri,
                    createdAt = state.createdAt,
                    savedAt = System.currentTimeMillis(),
                    totalCalories = state.totals.calories,
                    totalProteinG = state.totals.proteinG,
                    totalCarbsG = state.totals.carbsG,
                    totalFatG = state.totals.fatG,
                    overallConfidence = state.overallConfidence,
                    analysisStatus = AnalysisStatus.COMPLETED,
                    userEdited = state.userEdited,
                    foodItems = state.foods
                )
                mealRepository.saveMeal(meal)
                _saveSuccess.trySend(Unit)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = AppError.DatabaseError)
            } finally {
                _isSaving.value = false
            }
        }
    }
}

data class MealResultUiState(
    val id: String = "",
    val mealType: MealType = MealType.LUNCH,
    val imageUri: String? = null,
    val createdAt: Long = 0L,
    val foods: List<FoodItem> = emptyList(),
    val totals: NutritionTotals = NutritionTotals(0.0, 0.0, 0.0, 0.0),
    val overallConfidence: Float? = null,
    val userEdited: Boolean = false,
    val isLoading: Boolean = false,
    val error: AppError? = null
) {
    val hasContent: Boolean = error == null && foods.isNotEmpty()
    val isEmpty: Boolean = error == null && foods.isEmpty()
}
