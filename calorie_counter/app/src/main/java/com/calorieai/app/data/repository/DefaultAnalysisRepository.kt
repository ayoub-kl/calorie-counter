package com.calorieai.app.data.repository

import android.content.Context
import android.net.Uri
import com.calorieai.app.data.api.AnalysisApiService
import com.calorieai.app.data.api.AnalysisMapper
import com.calorieai.app.data.util.compressImageForUpload
import com.calorieai.app.domain.model.MealAnalysisResult
import com.calorieai.app.domain.model.MealType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Repository implementation that calls the analysis API and maps validated
 * response to domain [MealAnalysisResult]. Never exposes DTOs to UI.
 */
class DefaultAnalysisRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: AnalysisApiService
) : AnalysisRepository {

    override suspend fun analyzeMeal(
        mealType: MealType,
        imageUri: String
    ): Result<MealAnalysisResult> = runCatching {
        val uri = Uri.parse(imageUri)
        val (part, tempFile) = withContext(Dispatchers.IO) {
            context.createImagePart(uri) ?: throw IllegalArgumentException("Could not read image from URI")
        }
        try {
            val mealTypeBody = mealType.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val response = api.analyzeMealPhoto(part, mealTypeBody)
            val captureTimestamp = System.currentTimeMillis()
            AnalysisMapper.toDomain(
                dto = response,
                mealType = mealType,
                captureTimestamp = captureTimestamp,
                imageUri = imageUri
            ).getOrThrow()
        } finally {
            withContext(Dispatchers.IO) { tempFile.delete() }
        }
    }
}

/**
 * Creates a multipart part from a content URI. Compresses the image first (resize + JPEG)
 * to reduce upload size and memory; falls back to stream copy if compression fails.
 * Uses file-backed request body to avoid holding full image bytes in memory.
 * Returns (Part, tempFile); caller must delete tempFile after upload.
 * Call from IO dispatcher.
 */
private fun Context.createImagePart(uri: Uri): Pair<MultipartBody.Part, File>? {
    val fileToUpload = compressImageForUpload(this, uri)
        ?: run {
            val stream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File(cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            try {
                FileOutputStream(tempFile).use { out -> stream.use { it.copyTo(out) } }
                tempFile
            } catch (_: Exception) {
                tempFile.delete()
                return null
            }
        }
    val body = fileToUpload.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val part = MultipartBody.Part.createFormData("image", fileToUpload.name, body)
    return part to fileToUpload
}
