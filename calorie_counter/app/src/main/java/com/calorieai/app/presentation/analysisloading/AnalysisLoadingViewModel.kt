package com.calorieai.app.presentation.analysisloading

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorieai.app.data.AnalysisResultHolder
import com.calorieai.app.domain.model.AppError
import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import com.calorieai.app.navigation.NavRoutes
import com.calorieai.app.domain.usecase.AnalyzeMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisLoadingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val analyzeMealUseCase: AnalyzeMealUseCase,
    private val resultHolder: AnalysisResultHolder
) : ViewModel() {

    private val mealType: MealType = parseMealType(savedStateHandle.get<String>("mealType") ?: "")
    private val imageUri: String = NavRoutes.decodeImageUriFromRoute(savedStateHandle.get<String>("imageUri") ?: "")

    private val _uiState = MutableStateFlow<AnalysisLoadingUiState>(AnalysisLoadingUiState.Loading)
    val uiState: StateFlow<AnalysisLoadingUiState> = _uiState.asStateFlow()

    private val _navigateToResult = Channel<Unit>(Channel.CONFLATED)
    val navigateToResult = _navigateToResult.receiveAsFlow()

    private var hasTriggeredAnalysis = false

    fun runAnalysisOnce() {
        if (hasTriggeredAnalysis) return
        if (imageUri.isBlank()) {
            _uiState.value = AnalysisLoadingUiState.Error(AppError.InvalidAiResponse("Invalid image."))
            return
        }
        hasTriggeredAnalysis = true
        viewModelScope.launch {
            _uiState.value = AnalysisLoadingUiState.Loading
            analyzeMealUseCase(mealType, imageUri)
                .onSuccess { result ->
                    resultHolder.setResult(result)
                    _uiState.value = AnalysisLoadingUiState.Success
                    _navigateToResult.trySend(Unit)
                }
                .onFailure { e ->
                    _uiState.value = AnalysisLoadingUiState.Error(AppError.fromThrowable(e))
                }
        }
    }

    fun retry() {
        hasTriggeredAnalysis = false
        runAnalysisOnce()
    }

    private fun parseMealType(value: String): MealType = try {
        MealType.valueOf(value.uppercase())
    } catch (_: Exception) {
        MealType.LUNCH
    }
}

sealed interface AnalysisLoadingUiState {
    data object Loading : AnalysisLoadingUiState
    data object Success : AnalysisLoadingUiState
    data class Error(val error: AppError) : AnalysisLoadingUiState
}
