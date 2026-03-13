package com.calorieai.app.presentation.mealtypeselect

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MealTypeSelectViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MealTypeSelectUiState())
    val uiState: StateFlow<MealTypeSelectUiState> = _uiState.asStateFlow()
}

data class MealTypeSelectUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
