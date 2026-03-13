package com.calorieai.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.calorieai.app.domain.usecase.GetMealsUseCase
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMealsUseCase: GetMealsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getMealsUseCase.invoke().collect { meals ->
                _uiState.value = _uiState.value.copy(meals = meals, isLoading = false)
            }
        }
    }
}

data class HistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val meals: List<com.calorieai.app.domain.model.Meal> = emptyList()
)
