package com.calorieai.app.presentation.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraCaptureViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraCaptureUiState())
    val uiState: StateFlow<CameraCaptureUiState> = _uiState.asStateFlow()
}

data class CameraCaptureUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val permissionDenied: Boolean = false
)
