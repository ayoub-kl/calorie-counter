package com.calorieai.app.presentation.mealdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorieai.app.domain.model.Meal
import com.calorieai.app.domain.usecase.GetMealByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMealByIdUseCase: GetMealByIdUseCase
) : ViewModel() {

    private val mealId: String = savedStateHandle.get<String>("mealId") ?: ""

    private val _uiState = MutableStateFlow(MealDetailUiState(isLoading = true))
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    init {
        if (mealId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Invalid meal"
            )
        } else {
            viewModelScope.launch {
                getMealByIdUseCase.invoke(mealId)
                    .catch { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load meal"
                        )
                    }
                    .collect { meal ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = if (meal == null) "Meal not found" else null,
                            meal = meal
                        )
                    }
            }
        }
    }
}

data class MealDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val meal: Meal? = null
)