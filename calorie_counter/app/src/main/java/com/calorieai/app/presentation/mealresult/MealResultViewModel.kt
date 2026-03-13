package com.calorieai.app.presentation.mealresult

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MealResultViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MealResultUiState())
    val uiState: StateFlow<MealResultUiState> = _uiState.asStateFlow()
}

data class MealResultUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val content: Boolean = false
)
