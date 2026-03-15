package com.calorieai.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.calorieai.app.domain.model.AppError
import com.calorieai.app.ui.components.ErrorState
import android.content.pm.PackageManager
import java.io.File
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraCaptureScreen(
    onNavigateBack: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val onPhotoCapturedState = rememberUpdatedState(onPhotoCaptured)

    var hasPermission by remember { mutableStateOf(false) }
    var permissionDenied by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) permissionDenied = true
    }

    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var captureError by remember { mutableStateOf<AppError?>(null) }

    LaunchedEffect(Unit) {
        val hasCamera = context.packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_ANY)
        if (!hasCamera) captureError = AppError.CameraUnavailable
    }

    when {
        permissionDenied && !hasPermission -> ErrorState(
            message = AppError.PermissionDenied.userMessage,
            onRetry = { permissionDenied = false; permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            modifier = modifier
        )
        captureError != null -> ErrorState(
            message = captureError!!.userMessage,
            onRetry = if (captureError!!.recoverable) ({ captureError = null }) else null,
            modifier = modifier
        )
        !hasPermission -> Box(modifier = modifier.fillMaxSize()) {
            Text("Requesting camera permission…")
        }
        else -> Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Capture meal") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val cap = imageCapture ?: return@FloatingActionButton
                        val outputDir = File(context.cacheDir, "meal_photos").apply { mkdirs() }
                        val outputFile = File(outputDir, "meal_${System.currentTimeMillis()}.jpg")
                        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
                        cap.takePicture(
                            outputOptions,
                            Executors.newSingleThreadExecutor(),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                    val uri = try {
                                        FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            outputFile
                                        )
                                    } catch (_: Exception) {
                                        Uri.fromFile(outputFile)
                                    }
                                    val uriString = uri.toString()
                                    ContextCompat.getMainExecutor(context).execute {
                                        onPhotoCapturedState.value(uriString)
                                    }
                                }
                                override fun onError(exception: ImageCaptureException) {
                                    captureError = AppError.CameraUnavailable
                                }
                            }
                        )
                    }
                ) {
                    Icon(Icons.Filled.Camera, contentDescription = "Capture")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                var previewView by remember { mutableStateOf<PreviewView?>(null) }
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            previewView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                DisposableEffect(lifecycleOwner, previewView) {
                    val pv = previewView ?: return@DisposableEffect onDispose { }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(pv.surfaceProvider)
                        }
                        val imageCaptureUseCase = ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build()
                        imageCapture = imageCaptureUseCase
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageCaptureUseCase
                            )
                        } catch (_: Exception) {
                            captureError = AppError.CameraUnavailable
                        }
                    }, ContextCompat.getMainExecutor(context))
                    onDispose {
                        cameraProviderFuture.get().unbindAll()
                    }
                }
            }
        }
    }
}
