package com.calorieai.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.calorieai.app.domain.model.AppError
import com.calorieai.app.domain.usecase.GetMealsUseCase
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMealsUseCase: GetMealsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                getMealsUseCase.invoke().collect { meals ->
                    _uiState.value = _uiState.value.copy(meals = meals, isLoading = false, error = null)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = AppError.DatabaseError
                )
            }
        }
    }

    fun retry() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                getMealsUseCase.invoke().collect { meals ->
                    _uiState.value = _uiState.value.copy(meals = meals, isLoading = false, error = null)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = AppError.DatabaseError
                )
            }
        }
    }
}

data class HistoryUiState(
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val meals: List<com.calorieai.app.domain.model.Meal> = emptyList()
)
