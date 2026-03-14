package com.calorieai.app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraCaptureScreen(
    onNavigateBack: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Placeholder: CameraX UI in later prompts
    Text(text = "Camera Capture", modifier = modifier)
}
