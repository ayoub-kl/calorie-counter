package com.calorieai.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorieai.app.presentation.analysisloading.AnalysisLoadingUiState
import com.calorieai.app.presentation.analysisloading.AnalysisLoadingViewModel
import com.calorieai.app.ui.components.ErrorState
import com.calorieai.app.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisLoadingScreen(
    onNavigateBack: () -> Unit,
    onAnalysisComplete: () -> Unit,
    viewModel: AnalysisLoadingViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.runAnalysisOnce()
    }
    LaunchedEffect(Unit) {
        viewModel.navigateToResult.collect {
            onAnalysisComplete()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Analyzing meal") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is AnalysisLoadingUiState.Loading -> LoadingIndicator(
                    message = "Analyzing meal…",
                    modifier = Modifier.fillMaxSize()
                )
                is AnalysisLoadingUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { viewModel.retry() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                )
                is AnalysisLoadingUiState.Success -> LoadingIndicator(
                    message = "Done",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
